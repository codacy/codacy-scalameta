package codacy

import java.nio.file.{Files, Path}

import codacy.base.PatternCompanion
import codacy.dockerApi._
import codacy.macros.Patterns
import play.api.libs.json.{JsError, JsSuccess, Reads}

import scala.meta._
import scala.util.matching.Regex
import scala.util.{Success, Try}


object CodacyScalameta extends Tool {

  implicit val regexReader: Reads[Regex] = Reads(
    _.validate[String].flatMap { case raw =>
      Try(raw.r) match {
        case util.Success(regex) => JsSuccess(regex)
        case util.Failure(ex) => JsError(ex.getMessage)
      }
    }
  )

  val newPatterns: Map[PatternId, PatternCompanion] = Patterns.fromResources

  override def apply(path: Path, conf: Option[List[PatternDef]], files: Option[Set[Path]])(implicit spec: Spec): Try[List[Result]] = {
    val allFiles = {
      import scala.collection.JavaConversions._
      files.map(_.toList).getOrElse(Files.walk(path).iterator().toList)
    }

    val fNewPatterns: List[(PatternId, codacy.base.Pattern)] = {
      lazy val default = newPatterns.toList.map { case (pid, patternFactory) =>
        val pat: codacy.base.Pattern = patternFactory.fromConfiguration(patternFactory.defaultConfig)
        (pid, pat)
      }

      conf match {
        case Some(defs) =>
          defs.flatMap { case pDef: PatternDef =>
            val params: Set[ParameterDef] = pDef.parameters.getOrElse {
              spec.patterns.filter(_.patternId == pDef.patternId).
                flatMap(_.parameters.toList.map(_.map { case spec =>
                  ParameterDef(spec.name, spec.default)
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
        lazy val sourcePath = SourcePath(DockerEnvironment.sourcePath.relativize(path).toString)

        path.toFile().parse[Source] match {
          case Parsed.Success(tree) =>

            fNewPatterns.flatMap { case (patternId, pat) =>
              Try(pat.apply(tree)) match {
                case util.Success(resList) =>
                  resList.map { case res =>
                    Issue(sourcePath, ResultMessage(res.message.value), patternId, ResultLine(res.position.start.line + 1))
                  }.toSeq.distinct
                case util.Failure(err) =>
                  List(FileError(sourcePath, Option(ErrorMessage(err.getMessage))))
              }
            }

          case Parsed.Error(position, message, details) =>
            List(FileError(sourcePath, Option(ErrorMessage(message))))
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