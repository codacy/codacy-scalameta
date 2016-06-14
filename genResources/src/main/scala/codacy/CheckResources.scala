package codacy

import java.net.{URI, URLClassLoader}
import java.nio.file.FileSystemAlreadyExistsException

import better.files._
import codacy.dockerApi.{Spec, ToolName}
import codacy.macros.{JReadsMacro, Patterns}
import play.api.libs.json._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

object CheckResources{


  def main(args: Array[String]):Unit = {

    val fs = generated(new ToolName("scalaHomebrew"),File(args.head))
    println(s"generated ${fs.size} resources")
  }

  def allDescriptions(classLoader:URLClassLoader, dest:File) = {
    val uris = classLoader.findResources("docs/description").map{ case url => url.toURI }.toSet
    val allFs = uris.flatMap{ case uri =>

      val env = new java.util.HashMap[String,String]
      env.put("create", "true")

      Try(
        java.nio.file.FileSystems.newFileSystem(uri, env)
      ) match{
        case Success(fs) =>
          fs
        case Failure(ex: FileSystemAlreadyExistsException) =>
          ()
        case Failure(ex: IllegalArgumentException) =>
          ()
        case Failure(ex) =>
          Console.err.println(s"[${ex.getClass.getSimpleName}] - ${ex.getMessage}")
      }


      val file = File(uri)
      val descs = file.list.toList.filter(_.extension.exists(_ == ".md"))
      descs
    }

    val destFolder = (dest / "docs" / "description").createIfNotExists(asDirectory = true, createParents = true)

    allFs.map{ f =>
      f.copyTo(destFolder / f.name, overwrite = true)
    }
  }

  def allTests(classLoader:URLClassLoader, dest:File) = {
    val uris = classLoader.findResources("docs/tests").map{ case url => url.toURI }.toSet
    val allFs = uris.flatMap{ case uri =>
      val file = File(uri)
      val descs = file.list.toList
      descs
    }

    val destFolder = (dest / "docs" / "tests").createIfNotExists(asDirectory = true, createParents = true)

    allFs.map{ f =>
      f.copyTo(destFolder / f.name, overwrite = true)
    }
  }

  def allSources(classLoader:URLClassLoader, dest:File) = {
    val uris = classLoader.findResources("patterns").map{ case url => url.toURI }.toSet
    val allFs = uris.flatMap{ case uri =>
      val file = File(uri)
      val pats = file.list.toList
      pats
    }

    val destFolder = (dest / "docs" / "patterns").createIfNotExists(asDirectory = true, createParents = true)

    allFs.map{ f =>
      f.copyTo(destFolder / f.name, overwrite = true)
    }
  }

  def descriptionJson(classLoader:URLClassLoader, dest:File) = {
    val uris = classLoader.findResources("docs/description/description.json").map{ case url => url.toURI }.toSet
    val allJsons: List[JsValue] = uris.flatMap{ case uri =>
      Json.parse(File(uri).contentAsString).as[List[JsValue]]
    }.toList.sortBy(v => (v \ "patternId").asOpt[String] )

    val content = Json.prettyPrint(Json.toJson(allJsons))

    val destFile = (dest / "docs" / "description" / "description.json").createIfNotExists(asDirectory = false, createParents = true)

    destFile.overwrite(content)
  }

  def patternsJson(classLoader:URLClassLoader, dest:File, toolName: ToolName) = {

    val files = JReadsMacro.allPatternJsons(classLoader)

    val pats = files.toSet[File].flatMap{ case file =>
      val ps = Json.parse(file.contentAsString) \ "patterns"
      ps.as[Set[JsValue]]
    }.toList.sortBy{ case v => (v \ "patternId").as[String] }

    val jContent = Json.obj(
      "name" -> toolName.value,
      "patterns" -> pats
    )

    val content = Json.prettyPrint(jContent)
    val destFile = (dest / "docs" / "patterns.json").createIfNotExists(asDirectory = false, createParents = true)
    destFile.overwrite(content)
  }

  def generated(toolName:ToolName, destDir:File) = {
    getClass.getClassLoader match{
      case urlCs:URLClassLoader =>
        allDescriptions(urlCs, destDir).toList ++
        allSources(urlCs,destDir) ++
        allTests(urlCs, destDir) :+
        descriptionJson(urlCs,destDir) :+
        patternsJson(urlCs,destDir, toolName)
    }
  }
}