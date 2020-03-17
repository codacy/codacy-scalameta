package codacy.macros

import java.net.URLClassLoader

import codacy.base.PatternCompanion
import com.codacy.plugins.api._
import com.codacy.plugins.api.results.Pattern
import com.codacy.plugins.api.results.Tool
import codacy.utils.FileHelpers
import play.api.libs.json._

import scala.reflect.macros.whitebox

object Patterns{
  import scala.language.experimental.macros

  def fromResources:Map[Pattern.Id,PatternCompanion] = macro JReadsMacro.impl
  def params:Map[Pattern.Id,List[JsObject]] = macro JReadsMacro.impl2
}

private[macros] object JReadsMacro {

  implicit class ContextExtension(context:whitebox.Context){
    lazy val patternIds: Set[Pattern.Id] = {
      FileHelpers.allPatternJsons(new URLClassLoader(context.classPath.toArray))
    }.map{ json =>
      (Json.parse(json.contentAsString).as[JsObject] ++ Json.obj("name" -> "hb")).as[Tool.Specification]
    }.flatMap(_.patterns.map(_.patternId))
  }

  def impl2(c: whitebox.Context): c.Expr[Map[Pattern.Id,List[JsObject]]] = {
    import c.universe._

    implicit val _: c.universe.Liftable[Pattern.Id] =
      Liftable( (patternId:Pattern.Id) => q"new com.codacy.plugins.api.results.Pattern.Id(${patternId.value})")

    val tuples = c.patternIds.map{ pID =>
      val p = Literal(Constant(pID.value))
      q"""($pID, codacy.macros.PatternMacros.params($p))"""
    }

    c.Expr[Map[Pattern.Id,List[JsObject]]](q"Map(..$tuples)" )
  }

  def impl(c: whitebox.Context): c.Expr[Map[Pattern.Id,PatternCompanion]] = {
    import c.universe._

    implicit val _: c.universe.Liftable[Pattern.Id] =
      Liftable((patternId:Pattern.Id) => q"new com.codacy.plugins.api.results.Pattern.Id(${patternId.value})")

    val tuples = c.patternIds.map{ pID =>
      val p = Literal(Constant(pID.value))
      q"""($pID, codacy.macros.PatternMacros.companion($p))"""
    }

    c.Expr[Map[Pattern.Id,PatternCompanion]](q"Map(..$tuples)" )
  }
}
