package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.util.matching.Regex

class Custom_Scala_FieldNamesChecker(configuration: Custom_Scala_FieldNamesChecker.Configuration) extends Pattern {

  def this() = this(Custom_Scala_FieldNamesChecker.Configuration())

  override def apply(tree: Tree): Set[Result] = {
    tree
      .collect {
        //val definitions
        case t @ q"..$_ val ..$patsnel: $tpe = $expr"
            if configuration.includeEnums || !isEnumValDefRegexOrDecl(t.parent, Option(expr), tpe) =>
          conflictingNames(patsnel)
        //val declarations
        case t @ q"..$_ val ..$pnamesnel: $tpe"
            if configuration.includeEnums || !isEnumValDefRegexOrDecl(t.parent, tpe = Option(tpe)) =>
          conflictingNames(pnamesnel)
        //var definitions
        case t @ q"..$_ var ..$patsnel: $tpe = $expr"
            if configuration.includeEnums || !isEnumValDefRegexOrDecl(t.parent, expr, tpe) =>
          conflictingNames(patsnel)
        //var declarations
        case t @ q"..$_ var ..$pnamesnel: $tpe"
            if configuration.includeEnums || !isEnumValDefRegexOrDecl(t.parent, tpe = Option(tpe)) =>
          conflictingNames(pnamesnel)
        //parameter values
        case t @ param"..$mods $paramname: $atpeopt = $expropt" if isConflictingName(paramname) =>
          List(paramname)
      }
      .flatten
      .map { tree =>
        Result(message(tree), tree)
      }
      .toSet
  }

  private[this] def isEnumValDefRegexOrDecl(parent: Option[Tree], expr: Option[Tree] = None, tpe: Option[Type]) = {
    val extendsEnumeration: Boolean = parent
      .collect {
        case template"{ ..$stats } with ..${ctorcalls: Seq[Init]} { $param => ..$stats2 }" =>
          ctorcalls.exists {
            case init"Enumeration" => true
            case _ => false
          }
      }
      .getOrElse(false)

    lazy val exprIsValue: Boolean = expr.exists {
      case q"${Term.Name(value)}" => value == "Value"
      case q"${Term.Apply(Term.Name(name), _)}" => name == "Value"
      case _ => false
    }

    lazy val exprIsRegex: Boolean = expr.exists {
      case q"${lit: Lit}.r" => true
      case _ => false
    }

    lazy val tpeIsValue: Boolean = tpe.exists {
      case t"${Type.Name(value)}" => value == "Value"
      case _ => false
    }

    exprIsRegex || (extendsEnumeration && (exprIsValue || tpeIsValue))
  }

  private[this] def isConstant(tree: Tree): Boolean = tree.parent.exists {
    case t @ q"..$_ val ..$patsnel: $tpe = $expr" =>
      configuration.allowConstants && t.parent
        .flatMap(_.parent)
        .exists {
          case Defn.Object(_, _, _) => true
          case Pkg.Object(_, _, _) => true
          case _ => false
        }
    case _ => false
  }

  private[this] def conflictingNames(trees: Seq[Tree]) = {
    trees.collect {
      //tuples
      case p"(..$exprsnel)" =>
        exprsnel.filter(isConflictingName)
      case single if isConflictingName(single) =>
        List(single)
    }.flatten
  }

  private[this] def isConflictingName(tree: Tree): Boolean = {
    val name = tree match {
      case q"${term: Pat.Var}" =>
        Some(term.name.value)
      case q"${name: Term.Name}" =>
        Some(name.value)
      case t =>
        None
    }
    name.exists {
      case name if isConstant(tree) => !configuration.constantsRegex.pattern.matcher(name).matches()
      case name => !configuration.regex.pattern.matcher(name).matches()
    }
  }

  private[this] def message(tree: Tree) = {
    val regex = if (isConstant(tree)) configuration.constantsRegex else configuration.regex
    Message(s"Field name '$tree' does not match the regular expression '$regex'")
  }
}

object Custom_Scala_FieldNamesChecker {
  case class Configuration(
      regex: Regex = """^[a-z][A-Za-z0-9]*$""".r,
      includeEnums: Boolean = false,
      allowConstants: Boolean = true,
      constantsRegex: Regex = """^[A-Za-z][A-Za-z0-9]*$""".r
  )

}
