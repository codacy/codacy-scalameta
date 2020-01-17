package codacy

import java.net.URLClassLoader

import better.files._
import com.codacy.plugins.api._
import com.codacy.plugins.api.results._
import codacy.macros.Patterns
import codacy.utils.FileHelpers
import codacy.utils.FileHelpers._
import play.api.libs.json._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

object CheckResources {

  def main(args: Array[String]): Unit = {
    val fs = generated(Tool.Name("scalaHomebrew"), File(args.head))
    println(s"generated ${fs.size} resources")
  }

  private def allDescriptions(classLoader: URLClassLoader, dest: File) = {
    lazy val destFolder = (dest / "docs" / "description").createIfNotExists(asDirectory = true, createParents = true)

    classLoader
      .findResources("docs/description")
      .map(_.toFile)
      .flatMap { folder =>
        folder.list.filter(_.extension.exists(_ == ".md"))
      }
      .map { mdFile =>
        val destination = destFolder / mdFile.name
        mdFile.copyTo(destination, overwrite = true)
      }
  }

  private def allTests(classLoader: URLClassLoader, dest: File) = {
    lazy val destFolder = (dest / "docs" / "tests").createIfNotExists(asDirectory = true, createParents = true)

    classLoader
      .findResources("docs/tests")
      .map(_.toFile)
      .flatMap { folder =>
        folder.list
      }
      .map { testFile =>
        val destination = destFolder / testFile.name
        testFile.copyTo(destination, overwrite = true)
      }
  }

  private def allMultipleTests(classLoader: URLClassLoader, dest: File) = {
    val destFolder = (dest / "docs" / "multiple-tests").createIfNotExists(asDirectory = true, createParents = true)

    val multipleTestsResources = classLoader.findResources("docs/multiple-tests")
    val multipleTestsResFile = File(multipleTestsResources.nextElement().getFile)

    multipleTestsResFile
      .list(f => f.parent == multipleTestsResFile)
      .foreach(f => {
        f.copyTo(destFolder / f.name, overwrite = true)
      })
  }

  private def allSources(classLoader: URLClassLoader, dest: File) = {
    lazy val destFolder = (dest / "docs" / "patterns").createIfNotExists(asDirectory = true, createParents = true)

    classLoader
      .findResources("patterns")
      .map(_.toFile)
      .flatMap { folder =>
        folder.list
      }
      .map { sourceFile =>
        val destination = destFolder / sourceFile.name
        sourceFile.copyTo(destination, overwrite = true)
      }
  }

  private def descriptionsJson(classLoader: URLClassLoader, dest: File) = {
    val allDescriptions = classLoader
      .findResources("docs/description/description.json")
      .flatMap { uri =>
        val fileContent = uri.toFile.contentAsString
        Json.parse(fileContent).as[List[JsValue]]
      }
      .toList
      .distinct
      .sortBy(v => (v \ "patternId").asOpt[String])

    val destFile =
      (dest / "docs" / "description" / "description.json").createIfNotExists(asDirectory = false, createParents = true)

    destFile.overwrite(Json.prettyPrint(Json.toJson(allDescriptions)))
  }

  implicit val rWrites: Writes[Regex] = Writes((r: Regex) => Json.toJson(r.toString))

  private def patternsJson(classLoader: URLClassLoader, dest: File, toolName: Tool.Name) = {

    val files = FileHelpers.allPatternJsons(classLoader)
    lazy val params: Map[Pattern.Id, List[JsObject]] = Patterns.params

    val patterns = files
      .flatMap { file =>
        Json.parse(file.contentAsString).\("patterns").as[Set[JsObject]]
      }
      .map { jPattern =>
        val id = (jPattern \ "patternId").as[Pattern.Id]

        val patternParams = params.get(id).filter(_.nonEmpty)

        id -> patternParams
          .map { params =>
            jPattern ++ Json.obj("parameters" -> params)
          }
          .getOrElse(jPattern)

      }
      .toList
      .sortBy { case (id, _) => id.value }
      .map { case (_, json) => json }

    val content = Json.prettyPrint(Json.obj("name" -> toolName.value, "patterns" -> patterns))

    val destFile = (dest / "docs" / "patterns.json").createIfNotExists(asDirectory = false, createParents = true)
    destFile.overwrite(content)
  }

  private def generated(toolName: Tool.Name, destDir: File) = {
    getClass.getClassLoader match {
      case urlCs: URLClassLoader =>
        allDescriptions(urlCs, destDir).toList ++
          allSources(urlCs, destDir) ++
          allTests(urlCs, destDir) :+
          allMultipleTests(urlCs, destDir) :+
          descriptionsJson(urlCs, destDir) :+
          patternsJson(urlCs, destDir, toolName)
    }
  }
}
