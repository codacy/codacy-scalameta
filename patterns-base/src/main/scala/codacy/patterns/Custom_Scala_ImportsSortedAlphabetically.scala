package codacy.patterns

import codacy.base.Pattern
import scala.meta._

class Custom_Scala_ImportsSortedAlphabetically(config: Custom_Scala_ImportsSortedAlphabetically.Configuration)
    extends Pattern {

  def this() = this(Custom_Scala_ImportsSortedAlphabetically.Configuration())

  override def apply(tree: Tree): Iterable[Result] = {
    tree
      .collect[(Tree, Option[Importer])] {
        case t @ q"import ..${importersnel: Seq[Importer]}" => (t, importersnel.headOption)
      }
      .groupBy { case (tree, _) => tree.parent }
      .collect {
        case (Some(parent), importsWithImporters) =>
          lazy val treeToImporter: Map[Tree, Option[Importer]] = importsWithImporters.toMap

          parent.children
            .foldLeft(Seq.empty[Seq[Importer]]) {
              case (lists, elem) =>
                val currentLastList = lists.lastOption.getOrElse(Seq.empty)

                treeToImporter
                  .get(elem)
                  .map {
                    case importer =>
                      //found the elem -> it's an import, attach it to the last list
                      val newLastList = currentLastList ++ importer
                      lists.dropRight(1) :+ newLastList
                  }
                  .getOrElse {
                    //the element is NOT an import we have to start a new group
                    if (currentLastList.isEmpty) lists else lists :+ Seq.empty
                  }
            }
            .filter(_.nonEmpty)
      }
      .flatten
      .flatMap(firstUnsortedInBlock(_, config.caseSensitive))
      .map { case tree => Result(message(tree), tree) }
  }

  private[this] def firstUnsortedInBlock(block: Seq[Importer], caseSensitive: Boolean) = {

    def stringify(importer: Importer): String =
      if (caseSensitive) importer.toString() else importer.toString().toLowerCase()

    block
      .zip(block.drop(1))
      .dropWhile {
        case (i1, i2) =>
          stringify(i1) <= stringify(i2)
      }
      .collectFirst { case (_, importer) => importer }
  }

  private[this] def message(tree: Tree) = Message("Imports should be sorted alphabetically")
}

case object Custom_Scala_ImportsSortedAlphabetically {
  case class Configuration(caseSensitive: Boolean = false)
}
