package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_WildcardImportOnMany extends Pattern {

  private[this] lazy val maxImports = 6

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ importer"$ref.{..$importeesnel}" if importeesnel.length > maxImports =>
        Result(message(t), t)
    }
  }

  private[this] def message(numImports: Tree) = Message(s"Use wildcards when selecting more than $maxImports elements")
}
