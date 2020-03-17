package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.util.matching.Regex

class ObjectNamesChecker(config: ObjectNamesChecker.Configuration) extends Pattern {

  def this() = this(ObjectNamesChecker.Configuration())

  override def apply(tree: Tree): Iterable[Result] = {
    tree
      .collect {
        case mDecl @ Defn.Object(_, name, _) if !config.regex.pattern.matcher(name.value).matches() =>
          Result(Message(s"Object name does not match the regular expression '${config.regex}'"), mDecl)
      }
      .to[Set]
  }
}

case object ObjectNamesChecker {

  case class Configuration(regex: Regex = "^[A-Z][A-Za-z0-9]*$".r)

}
