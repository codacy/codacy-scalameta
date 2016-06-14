package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_NameResultsAndParameters extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"$_.$name" if unnamed.contains(name.toString) =>
        Result(message(t),t)
    }
  }

  private[this] lazy val unnamed: Set[String] = (1 to 22).map{ case i => s"_$i" }.toSet

  private[this] def message(tree: Tree) = Message("Found unnamed result or parameter")
}
