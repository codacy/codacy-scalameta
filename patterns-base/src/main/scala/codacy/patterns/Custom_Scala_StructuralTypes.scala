package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_StructuralTypes extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case tpe@t"$tpeopt { ..$stats }" if stats.nonEmpty =>
        Result(message(tpe), tpe)
    }
  }

  private[this] def message(name: Tree) = Message(s"Avoid using structural types")
}