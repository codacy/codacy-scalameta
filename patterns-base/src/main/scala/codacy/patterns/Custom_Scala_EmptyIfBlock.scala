package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_EmptyIfBlock extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{
      case t@q"if ($_) $expr else $oexpr"
        if expr.isEqual(q"{}") ||
        oexpr.isEqual(q"{}") =>
          Result(message(t),t)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using empty blocks in if statements")
}