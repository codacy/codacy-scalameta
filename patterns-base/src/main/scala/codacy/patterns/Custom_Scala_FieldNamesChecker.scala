package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.util.matching.Regex

class Custom_Scala_FieldNamesChecker(configuration: Custom_Scala_FieldNamesChecker.Configuration) extends Pattern{

  def this() = this(Custom_Scala_FieldNamesChecker.Configuration())

  override def apply(tree: Tree): Set[Result] = {
    tree.collect{
      //val definitions
      case t@q"..$_ val ..$patsnel: $tpe = $expr" if configuration.includeEnums || ! isEnumValDefRegexOrDecl(t.parent,Option(expr),tpe) =>
        conflictingNames(patsnel, isConstant(t))
      //val declarations
      case t@q"..$_ val ..$pnamesnel: $tpe" if configuration.includeEnums || ! isEnumValDefRegexOrDecl(t.parent,tpe=Option(tpe)) =>
        conflictingNames(pnamesnel)
      //var definitions
      case t@q"..$_ var ..$patsnel: $tpe = $expr" if configuration.includeEnums || ! isEnumValDefRegexOrDecl(t.parent,expr,tpe) =>
        conflictingNames(patsnel)
      //var declarations
      case t@q"..$_ var ..$pnamesnel: $tpe" if configuration.includeEnums || ! isEnumValDefRegexOrDecl(t.parent,tpe=Option(tpe)) =>
        conflictingNames(pnamesnel)
      //parameter values
      case t@param"..$mods $paramname: $atpeopt = $expropt" if isConflictingName(paramname) =>
        List(paramname)
    }.flatten.map{ case tree => Result(message(tree),tree) }.toSet
  }

  private[this] def isEnumValDefRegexOrDecl(parent:Option[Tree],expr:Option[Tree]=None,tpe:Option[Tree]=None) = {
    val extendsEnumeration:Boolean = parent.collect{ case template"{ ..$stats } with ..${ctorcalls:Seq[Ctor.Call]} { $param => ..$stats2 }" =>
      ctorcalls.exists{
        case ctor"Enumeration" => true
        case _ => false
      }
    }.getOrElse(false)

    lazy val exprIsValue:Boolean = expr.exists{
      case q"${Term.Name(value)}" => value == "Value"
      case q"${Term.Apply(Term.Name(name), _)}" => name == "Value"
      case _ => false
    }

    lazy val exprIsRegex:Boolean = expr.exists{
      case q"${lit: Lit}.r" => true
      case _ => false
    }

    lazy val tpeIsValue:Boolean = tpe.exists{
      case t"${Type.Name(value)}" => value == "Value"
      case _ => false
    }

    exprIsRegex || (extendsEnumeration && (exprIsValue || tpeIsValue))
  }

  private[this] def isConstant(tree: Tree): Boolean = {
    lazy val isConstant = tree.parent.flatMap(_.parent).collect {
      case q"..$_ object $_ extends $_" => true
      case q"package object $_ extends $_" => true
    }.getOrElse(false)

    configuration.allowConstants && isConstant
  }


  private[this] def conflictingNames(trees: Seq[Tree], isConstant: Boolean = false) = {
    trees.collect {
      //tuples
      case p"(..$exprsnel)" =>
        exprsnel.filter(isConflictingName(_, isConstant))
      case single if isConflictingName(single, isConstant) =>
        List(single)
    }.flatten
  }

  private[this] def isEscaped(name:Term.Name):Boolean = {
    name.tokens.collectFirst{ case id:Token.Ident => id.syntax }.
      exists{ case value => value.startsWith("`") && value.endsWith("`")}
  }

  private[this] def isConflictingName(tree: Tree, isConstant: Boolean = false): Boolean = {
    Option(tree).collect{
      case q"${term: Pat.Var.Term}" if ! isEscaped(term.name) =>
        term.name.value
      case q"${name: Term.Name}" if ! isEscaped(name) =>
        name.value
    }.exists {
      case name if isConstant => !configuration.constantsRegex.pattern.matcher(name).matches()
      case name => !configuration.regex.pattern.matcher(name).matches()
    }
  }

  private[this] def message(tree:Tree) = Message(s"Field name '$tree' does not match the regular expression '${configuration.regex}'")
}

object Custom_Scala_FieldNamesChecker{
  case class Configuration(regex:Regex="""^[a-z][A-Za-z0-9]*$""".r, includeEnums:Boolean=false,
                           allowConstants: Boolean = true, constantsRegex: Regex = """^[A-Za-z][A-Za-z0-9]*$""".r)

}
