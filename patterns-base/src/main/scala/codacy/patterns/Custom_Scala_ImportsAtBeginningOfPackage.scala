package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ImportsAtBeginningOfPackage extends Pattern{


  override def apply(tree: Tree) = {
    tree.collect{
      case q"package $ref { ..${stats:Seq[Tree]} }" =>
        stats.dropWhile( isImport ).flatMap(
          _.collect{
            case t:Tree if isImport(t) =>
              Result(message(t),t)
          }
      )
    }.flatten.toSet
  }

  private[this] def isImport(tree:Tree): Boolean = tree match{
    case q"import ..$importersnel" => true
    case _ => false
  }

  private[this] def message(tree: Tree) = Message("Put imports in the beginning of the package")
}
