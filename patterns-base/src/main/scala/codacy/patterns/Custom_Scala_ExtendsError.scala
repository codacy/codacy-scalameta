package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ExtendsError extends Pattern {

  override def apply(tree: Tree): List[Result] = {
    tree.collect {

      case t @ q"..$mods class $name (..$paramss) extends ..$supers {..$body}" if isOffender(supers) =>
        Result(message(t), t)
    }
  }

  def isOffender(supers: Seq[Init]): Boolean = {
    val supersName: Seq[String] = supers.map(_.toString())

    supersName.contains("Error") || supersName.contains("java.lang.Error")
  }

  private[this] def message(tree: Tree) = Message("java.lang.Error should not be extended")
}
