package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._

case object Custom_Scala_DirectPromiseCreation extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"Promise[$tpe]" =>
        Result(message(t),t)
      case t@q"Promise.$something" if forbiddenMethods.contains(something.toString) =>
        Result(message(t),t)
    }
  }

  private[this] lazy val forbiddenMethods = Set("apply", "failed", "fromTry", "successful")

  private[this] def message(tree: Tree) = Message("Avoid creating Promises directly.")
}