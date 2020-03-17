package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_CollectionLastHead extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ Term.Select(_, n @ Term.Name(name))
          if Set("head", "last").contains(name) && !t.parent.exists(_.isInstanceOf[Term.Apply]) =>
        Result(message(n), t)
    }
  }

  private[this] def message(name: Name) = Message(s"Usage of $name on collections.")
}
