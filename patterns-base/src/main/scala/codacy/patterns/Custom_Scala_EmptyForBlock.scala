package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_EmptyForBlock extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{
      case q"for (..$_) $expr"
        if expr.isEqual(q"{}") =>
      Result(message(expr),expr)
      
      case q"for (..$_) yield $expr"
        if expr.isEqual(q"{}") =>
      Result(message(expr), expr)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using empty blocks in for statements")
}