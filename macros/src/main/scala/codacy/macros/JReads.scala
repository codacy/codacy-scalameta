package codacy.macros

import java.net.URLClassLoader

import codacy.base.PatternCompanion
import codacy.dockerApi.{PatternId, Spec}
import codacy.utils.FileHelpers
import play.api.libs.json._

import scala.reflect.macros.whitebox._

object Patterns{
  import scala.language.experimental.macros

  def fromResources:Map[PatternId,PatternCompanion] = macro JReadsMacro.impl
  def params:Map[PatternId,List[JsObject]] = macro JReadsMacro.impl2
}

private[macros] object JReadsMacro {

  implicit class ContextExtension(context:Context){
    lazy val patternIds = {
      FileHelpers.allPatternJsons(new URLClassLoader(context.classPath.toArray))
    }.map{ case json =>
      (Json.parse(json.contentAsString).as[JsObject] ++ Json.obj("name" -> "hb")).as[Spec]
    }.flatMap(_.patterns.map(_.patternId))
  }

  def impl2(c: Context): c.Expr[Map[PatternId,List[JsObject]]] = {
    import c.universe._

    implicit val l = Liftable( (patternId:PatternId) => q"new codacy.dockerApi.PatternId(${patternId.value})")

    val tuples = c.patternIds.map{ case pID =>
      val p = Literal(Constant(pID.value))
      q"""($pID, codacy.macros.Pattern.params($p))"""
    }

    c.Expr[Map[PatternId,List[JsObject]]](q"Map(..$tuples)" )
  }

  def impl(c: Context): c.Expr[Map[PatternId,PatternCompanion]] = {
    import c.universe._

    implicit val l = Liftable( (patternId:PatternId) => q"new codacy.dockerApi.PatternId(${patternId.value})")

    val tuples = c.patternIds.map{ case pID =>
      val p = Literal(Constant(pID.value))
      q"""($pID, codacy.macros.Pattern.companion($p))"""
    }

    c.Expr[Map[PatternId,PatternCompanion]](q"Map(..$tuples)" )
  }
}
