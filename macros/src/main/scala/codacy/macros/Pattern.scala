package codacy.macros

import codacy.base.PatternCompanion

import scala.reflect.macros.whitebox._

object Pattern{
  import scala.language.experimental.macros
  def companion(id:String,jsonT:String):PatternCompanion = macro codacy.macros.PatternCompanionMacro.impl
}

private[macros] object PatternCompanionMacro {

  def impl(c: Context)(id:c.Expr[String],jsonT:c.Expr[String]): c.Expr[PatternCompanion] = {
    import c.universe._

    val (patternName,jsonConfig) = (id.tree,jsonT.tree) match {
      case (Literal(Constant(id: String)),Literal(Constant(json: String))) =>
        (id,json)
      case _ =>
        c.abort(c.enclosingPosition,"You must provide a literal resource path for schema parsing.")
    }

    val patternCompanionApply = q"codacy.base.PatternCompanion.apply"

    val fullName = s"codacy.patterns.$patternName"

    val Pattern = c.mirror.staticClass(fullName)

    def configPattern(m: MethodSymbol) = {
      val param = m.paramLists.head.head.asTerm
      val ConfigT = c.mirror.staticClass(param.typeSignature.typeSymbol.fullName)

      q"""{
        import play.api.libs.json._
        implicit val reads = Json.reads[$ConfigT]
        val cfg = Json.parse($jsonConfig).as(reads)

        $patternCompanionApply(
          cfg,
          (c:$ConfigT) => new $Pattern(c)
        )
      }"""
    }

    Pattern.typeSignature.decls.collectFirst{
      case m: MethodSymbol if m.isConstructor && m.paramLists.length == 1 && m.paramLists.head.length == 1 =>
        val expr = configPattern(m)
        c.Expr[PatternCompanion](expr)

      case m: MethodSymbol if m.isConstructor && m.paramLists.length == 1 && m.paramLists.head.length == 0 =>
        val expr = q"""$patternCompanionApply( new $Pattern() )"""
        c.Expr[PatternCompanion](expr)

    }.getOrElse{
      //it's an object
      val PatternModule = c.mirror.staticModule(fullName)
      val expr = q"""$patternCompanionApply[${PatternModule.typeSignature}]( ${PatternModule.asTerm} )"""

      c.Expr[PatternCompanion](expr)
    }
  }
}