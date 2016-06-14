package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ElseIf extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"if ($_) $_ else if ($_) $_ else $_" =>
        Result(message(t),t)
    }
  }

  private[this] def message(tree: Tree) = Message("Consider using case matching instead of else if blocks")
}