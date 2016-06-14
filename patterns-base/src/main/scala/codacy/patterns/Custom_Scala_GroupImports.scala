package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_GroupImports extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t: Template =>
        t.stats.getOrElse(Seq.empty)
      case q"package $ref { ..$stats }" =>
        stats
      case t: Term.Block =>
        t.stats
    }.map(imports).flatMap(duplicatedImporters).map { case tree =>
      Result(message(tree), tree)
    }
  }

  private[this] def duplicatedImporters(imports: Seq[Tree]) = {
    val tupled = imports.collect { case t@importer"$ref.{..$importeesnel}" => (ref, t) }

    tupled.groupBy { case (ref, _) => ref.toString }.collect {
      case (_, importers) if importers.length > 1 =>

        importers
          .map { case (_, importer) => importer }
          .sortBy { (importer:Tree) => (importer.pos.start.line, importer.pos.start.column) }
          .take(1)
    }.flatten
  }

  private[this] def imports(body: Seq[Tree]) = {
    body.collect { case t@q"import ..$importersnel" => importersnel }.flatten
  }

  private[this] def message(tree: Tree) = Message("Use brackets to group imports from the same package")

}
