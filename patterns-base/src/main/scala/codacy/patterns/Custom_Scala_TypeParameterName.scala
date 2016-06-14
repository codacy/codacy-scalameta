package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._
import scala.util.matching.Regex

class Custom_Scala_TypeParameterName(conf:Custom_Scala_TypeParameterName.Configuration=Custom_Scala_TypeParameterName.Configuration())
  extends Pattern{

  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect {
      case t@tparam"..$mods $tparamname[..$tparams] >: $tpeopt1 <: $tpeopt2 <% ..$tpes1 : ..$tpes2" if isOffender(tparamname) =>
        Result(message(tparamname), tparamname)
    }
  }

  private[this] def isOffender(tparam: Type.Param.Name): Boolean = {
    !conf.regex.pattern.matcher(tparam.toString()).matches()
  }

  private[this] def message(tree: Tree) = Message(s"Type parameter '$tree' does not conform with naming standard.")
}

case object Custom_Scala_TypeParameterName{
  // private[this] val C#StyleRegex = """^T[A-Z][A-Za-z0-9]*$""".r
  case class Configuration(regex: Regex="""^[A-Z]$""".r)
}
