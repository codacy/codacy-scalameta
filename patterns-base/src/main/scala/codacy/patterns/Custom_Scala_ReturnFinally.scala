package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ReturnFinally extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"try $expr catch { ..case $cases } finally ${expropt: Option[Term]}" =>
        findReturn(expropt).map { ret =>
          Result(message, ret)
        }
    }.flatten
  }

  private def findReturn(term: Option[Term]): Option[Tree] = {
    term.flatMap(_.collect {
      case t @ q"return $expr" =>
        t
    }.headOption)
  }

  private[this] def message = Message("Consider using case matching instead of else if blocks")
}
