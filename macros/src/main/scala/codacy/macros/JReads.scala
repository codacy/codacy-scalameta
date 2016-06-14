package codacy.macros

import java.net.{URI, URLClassLoader}
import java.nio.file.FileSystemAlreadyExistsException

import better.files._
import codacy.base.PatternCompanion
import codacy.dockerApi.{ParameterSpec, PatternId, PatternSpec, Spec}
import play.api.libs.json._

import scala.collection.JavaConversions._
import scala.language.experimental.macros
import scala.reflect.macros.whitebox._
import scala.util.{Failure, Success, Try}

object Patterns{
  def fromResources:Map[PatternId,PatternCompanion] = macro JReadsMacro.impl
}

object JReadsMacro {

  def allPatternJsons(classLoader: URLClassLoader) = {
    classLoader.findResources("patterns.json").toList.map{ case url =>
      val uri: URI = url.toURI
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

      File(java.nio.file.Paths.get(uri))
    }
  }

  def impl(c: Context): c.Expr[Map[PatternId,PatternCompanion]] = {
    import c.universe._

    val loader = new URLClassLoader(c.classPath.toArray)

    val tuples = allPatternJsons(loader).flatMap{ case json =>
      (Json.parse(json.contentAsString).as[JsObject] ++ Json.obj("name" -> "hb")).asOpt[Spec]
    }.flatMap{ case spec =>
      spec.patterns.map{ case spec => (spec.patternId.value, spec.parameters.getOrElse(Set.empty))}
    }.map{ case (id,params) =>
      val p = Literal(Constant(id))

      val jsonConfig = JsObject(params.toList.map{ case fields =>
        fields.name.value -> fields.default
      }.toMap)

      val jString = Literal(Constant(Json.stringify(jsonConfig)))

      q"""(new codacy.dockerApi.PatternId($id), codacy.macros.Pattern.companion($p,$jString))"""
    }

    c.Expr[Map[PatternId,PatternCompanion]](q"Map(..$tuples)" )
  }
}
