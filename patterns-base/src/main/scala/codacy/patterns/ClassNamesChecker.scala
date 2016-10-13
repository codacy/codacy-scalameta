package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._
import scala.util.matching.Regex

class ClassNamesChecker(config: ClassNamesChecker.Configuration) extends Pattern {

  def this() = this(ClassNamesChecker.Configuration())

  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect { case mDecl@Defn.Class(_, name, _, _, _) if ! config.regex.pattern.matcher(name.value).matches() =>
      Result(Message(s"Class name does not match the regular expression '${config.regex}'"), mDecl)
    }.to[Set]
  }
}

case object ClassNamesChecker {

  case class Configuration(regex: Regex = "^[A-Z][A-Za-z0-9]*$".r)

}
