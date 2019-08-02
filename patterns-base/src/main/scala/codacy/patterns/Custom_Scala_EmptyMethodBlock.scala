package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_EmptyMethodBlock extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{
      case t@q"..$_ def $_[..$_](...$_): $_ = $expr"
        if expr.isEqual(q"{}") =>
      Result(message(t),t)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using empty methods")
}