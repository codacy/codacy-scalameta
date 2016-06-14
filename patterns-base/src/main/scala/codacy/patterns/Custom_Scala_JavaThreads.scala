package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_JavaThreads extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case q"new { ..$stat } with ..$ctorcalls { $param => ..$stats }" if ctorcalls.length == 1 =>
        ctorcalls.headOption.collect{
          case t@ctor"$ctorref(..$aexprssnel)" if ctorref.toString == "Thread" =>
            Result(message(t),t)
        }

    }.flatten
  }

  private[this] def message(tree:Tree) = Message(s"Avoid creating Threads")
}