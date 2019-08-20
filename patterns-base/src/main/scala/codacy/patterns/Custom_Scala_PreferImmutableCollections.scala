package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_PreferImmutableCollections extends Pattern {

  override def apply(tree: Tree) = {
    tree
      .collect {
        case t @ q"import ..$importersnel" =>
          importersnel.collect {
            case imp if imp.toString.contains("collection.mutable") => imp
          }

        case t @ p"$expr.$name" if expr.toString == "mutable" || expr.toString.contains("collection.mutable") => List(t)

      }
      .flatten
      .map { case tree => Result(message(tree), tree) }
  }

  private[this] def message(tree: Tree) =
    Message(s"It's preferred to use immutable collections instead of mutable ones")
}
