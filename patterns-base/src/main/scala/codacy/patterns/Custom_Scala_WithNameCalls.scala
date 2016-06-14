package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_WithNameCalls extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"$_.withName(..$argss)" if argss.length == 1 /*&& argss.headOption.exists(_.length == 1)*/ =>
        Result(message(t),t)
    }
  }

  private[this] def message(tree: Tree) = Message("Usage of withName on Enumeration type.")
}