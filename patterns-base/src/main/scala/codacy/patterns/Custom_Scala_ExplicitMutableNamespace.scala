package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ExplicitMutableNamespace extends Pattern {

  override def apply(tree: Tree) = {
    val mutableImportSelectors: List[Importee] = tree.collect {
      case t @ importer"$ref.{..$importeesnel}" if ref.toString.contains("collection.mutable") =>
        importeesnel
    }.flatten

    val wildcards = mutableImportSelectors.collect {
      case t @ importee"_" =>
        Result(message(t), t)
    }.toSet

    wildcards ++ tree.collect {
      case t @ Term.Apply(Term.Name(name), Nil) =>
        mutableImportSelectors.collectFirst {
          case imp if imp.toString() == name =>
            Result(message(imp), imp)
        }
    }.flatten
  }

  private[this] def message(tree: Tree) = Message("Use the mutable namespace explicitly")
}
