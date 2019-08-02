package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.meta.contrib._

case object Custom_Scala_EmptyTryBlock extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    def matchCatch(expr: Term): Boolean = {
      expr match {
        case q"$_ {}" => true
        case _ => false
      }
    }

    tree.collect{
      case t@q"try $expr catch { ..case $cases } finally $expropt"
        if expr.isEqual(q"{}") || cases.isEmpty =>
        Result(message(t),t)

      case t@q"try $expr catch $exprc finally $expropt"
        if (expr.isEqual(q"{}") || matchCatch(exprc)) =>
        Result(message(t),t)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using empty try blocks")
}