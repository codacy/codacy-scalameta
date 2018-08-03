package codacy

import java.nio.file.{Files, Path, Paths}

import codacy.base.PatternCompanion
import com.codacy.plugins.api.{Source=>ToolSource,_}
import com.codacy.plugins.api.results._
import com.codacy.tools.scala.seed.DockerEnvironment
import codacy.macros.Patterns
import play.api.libs.json.{JsError, JsSuccess, Reads}

import scala.meta._
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}


object CodacyScalameta extends Tool {

  implicit val regexReader: Reads[Regex] = Reads(
    _.validate[String].flatMap { case raw =>
      Try(raw.r) match {
        case util.Success(regex) => JsSuccess(regex)
        case util.Failure(ex) => JsError(ex.getMessage)
      }
    }
  )

  val newPatterns: Map[Pattern.Id, PatternCompanion] = Patterns.fromResources

  override def apply(source: ToolSource.Directory,
                     configuration: Option[List[Pattern.Definition]],
                     files: Option[Set[ToolSource.File]],
                     options: Map[Options.Key, Options.Value])(implicit specification: Tool.Specification): Try[List[Result]] = {
    val allFiles: List[Path] = {
      import scala.collection.JavaConversions._
      files.map(_.toList.map(p => Paths.get(p.path))).getOrElse(Files.walk(Paths.get(source.path)).iterator().toList)
    }

    val fNewPatterns: List[(Pattern.Id, codacy.base.Pattern)] = {
      lazy val default = newPatterns.toList.map { case (pid, patternFactory) =>
        val pat: codacy.base.Pattern = patternFactory.fromConfiguration(patternFactory.defaultConfig)
        (pid, pat)
      }

      configuration match {
        case Some(defs) =>
          defs.flatMap { case pDef: Pattern.Definition =>
            val params: Set[Parameter.Definition] = pDef.parameters.getOrElse {
              specification.patterns.filter(_.patternId == pDef.patternId).
                flatMap(_.parameters.toList.map(_.map { case spec =>
                  Parameter.Definition(spec.name, spec.default)
                }).flatten)
            }

            newPatterns.get(pDef.patternId).map { case patternFactory =>
              patternFactory.fromParameters(params)
            }.collect { case Success(pat: codacy.base.Pattern) =>
              (pDef.patternId, pat)
            }
          }
        case _ => default
      }
    }

    val allResults = allFiles.flatMap {
      case path if isScala(path) =>
        lazy val sourcePath = ToolSource.File(new DockerEnvironment().defaultRootFile.relativize(path).toString)

        Try(path.toFile().parse[Source]) match {
          case Success(Parsed.Success(tree)) =>

            fNewPatterns.flatMap { case (patternId, pat) =>
              Try(pat.apply(tree)) match {
                case util.Success(resList) =>
                  resList.map { case res =>
                    Result.Issue(sourcePath, Result.Message(res.message.value), patternId, ToolSource.Line(res.position.start.line + 1))
                  }.toSeq.distinct
                case util.Failure(err) =>
                  List(Result.FileError(sourcePath, Option(ErrorMessage(err.getMessage))))
              }
            }

          case Success(Parsed.Error(position, message, details)) =>
            List(Result.FileError(sourcePath, Option(ErrorMessage(message))))

          case Failure(error) =>
            List(Result.FileError(sourcePath, Option(ErrorMessage(error.getMessage))))
        }

      case _ => List.empty
    }

    Try(allResults)
  }

  def isScala(path: Path): Boolean = {
    Files.isRegularFile(path) &&
      //pity that  Files.probeContentType(path) returns null;
      path.getFileName.toString.split('.').lastOption.exists(_ == "scala")
  }
}