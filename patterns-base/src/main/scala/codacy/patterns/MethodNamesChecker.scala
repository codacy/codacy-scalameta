package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._
import scala.util.matching.Regex

class MethodNamesChecker(config: MethodNamesChecker.Configuration) extends Pattern {

  def this() = this(MethodNamesChecker.Configuration())

  override def apply(tree: Tree): Iterable[Result] = {
    tree
      .collect {
        case mDecl @ Defn.Def(_, name, _, _, _, _) if !config.regex.pattern.matcher(name.value).matches() =>
          Result(Message(s"Method name does not match the regular expression '${config.regex}'"), mDecl)
      }
      .to[Set]
  }
}

case object MethodNamesChecker {

  case class Configuration(
      regex: Regex = """^([a-z][A-Za-z0-9]*|<<|>>>|>|==|!=|<|<=|>|>=|\||&|\^|\+|-|\*|\/|%|\|\||&&|\+\+|--|\+=|-=)$""".r
  )

}
