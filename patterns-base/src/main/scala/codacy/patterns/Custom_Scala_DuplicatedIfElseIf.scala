package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_DuplicatedIfElseIf extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{
      case t@q"if (${cond: Term}) $_ else if (${elsecond: Term}) $_ else $_"
           if cond.isEqual(elsecond) =>
        Result(message(cond),t)
    }
  }

  private[this] def message(tree: Tree) = Message(s"Duplicated condition '$tree' on if and else if statements")
}