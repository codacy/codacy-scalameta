package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_NotUsingNonEmpty extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"$_.length > 0"  =>
        Result(message(t), t)
      case t@q"$_.size > 0"  =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("Use nonEmpty instead of verifying that length is greater than 0 explicitly.")
}