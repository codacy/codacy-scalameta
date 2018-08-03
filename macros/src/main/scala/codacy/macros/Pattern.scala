package codacy.macros

import codacy.base.PatternCompanion
import play.api.libs.json.JsObject

import scala.reflect.macros.whitebox._

object PatternMacros{
  import scala.language.experimental.macros

  def companion(id:String):PatternCompanion = macro codacy.macros.PatternCompanionMacro.impl
  def params(id:String):List[JsObject] = macro PatternCompanionMacro.paramsImpl
}

private[macros] object PatternCompanionMacro {

  def paramsImpl(c: Context)(id:c.Expr[String]): c.Expr[List[JsObject]] = {
    import c.universe._

    val patternName = (id.tree) match {
      case (Literal(Constant(id: String))) =>
        id
      case _ =>
        c.abort(c.enclosingPosition,"You must provide a literal resource path for schema parsing.")
    }

    val fullName = s"codacy.patterns.$patternName"
    val Pattern = c.mirror.staticClass(fullName)
    val js = Pattern.typeSignature.decls.collectFirst{
      case m: MethodSymbol if m.isConstructor && m.paramLists.length == 1 && m.paramLists.head.length == 1 =>
        val param = m.paramLists.head.head.asTerm
        val ConfigT = c.mirror.staticClass(param.typeSignature.typeSymbol.fullName)
        val moduleSym = ConfigT.asType.companion

        ConfigT.typeSignature.decls.filter(_.isConstructor).flatMap{ case constr =>
          val params = constr.asMethod.paramLists.head

          assert(params.map(_.asTerm).forall(_.isParamWithDefault))

          params.map(_.asTerm).zipWithIndex.map{ case (param,i) =>
            val getterName = TermName("apply$default$" + (i + 1))
            q"""play.api.libs.json.Json.obj(
               "name"    -> ${param.name.toString},
               "default" -> $moduleSym.$getterName
            )"""
          }
        }
    }.getOrElse(List.empty)

    c.Expr(q"List( ..$js)")
  }

  def impl(c: Context)(id:c.Expr[String]): c.Expr[PatternCompanion] = {
    import c.universe._

    val patternName = id.tree match {
      case Literal(Constant(id: String)) =>
        id
      case _ =>
        c.abort(c.enclosingPosition,"You must provide a literal resource path for schema parsing.")
    }

    val patternCompanionApply = q"codacy.base.PatternCompanion.apply"

    val fullName = s"codacy.patterns.$patternName"

    val Pattern = c.mirror.staticClass(fullName)

    def configPattern(m: MethodSymbol) = {
      val param = m.paramLists.head.head.asTerm
      val ConfigT = c.mirror.staticClass(param.typeSignature.typeSymbol.fullName)

      val moduleSym = ConfigT.asType.companion

      val default = ConfigT.typeSignature.decls.find(_.isConstructor).map{ case constr =>
        val params = constr.asMethod.paramLists.head
        val defaultParams = params.map(_.asTerm).zipWithIndex.map { case (param, i) if param.isParamWithDefault =>
          val getterName = TermName("apply$default$" + (i + 1))
          q"$moduleSym.$getterName"
        }
        q"new $ConfigT( ..$defaultParams )"
      }.getOrElse(c.abort(c.enclosingPosition,"invalid argument type"))

      q"""{
        import play.api.libs.json._
        implicit val reads = Json.reads[$ConfigT]

        $patternCompanionApply(
          $default,
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