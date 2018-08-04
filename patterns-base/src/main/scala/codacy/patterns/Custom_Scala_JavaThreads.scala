package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_JavaThreads extends Pattern{

  override def apply(tree: Tree): List[Result] = {
    tree.collect{
      case q"new $init" =>
        Option(init).collect{
          case t@init"$tpe(...$exprss)" if tpe.toString == "Thread" =>
            Result(message(t),t)
        }

    }.flatten
  }

  private[this] def message(tree:Tree) = Message(s"Avoid creating Threads")
}