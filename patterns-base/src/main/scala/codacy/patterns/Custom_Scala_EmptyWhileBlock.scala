package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_EmptyWhileBlock extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{
      case q"while ($_) $expr"
        if expr.isEqual(q"{}") =>
	  Result(message(expr),expr)
	  case q"do $expr while ($_)"
        if expr.isEqual(q"{}") =>
      Result(message(expr),expr)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using empty blocks in while statements")
}