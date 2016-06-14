package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_GetCalls extends Pattern{

  override def apply(tree: Tree) = {

    tree.collect{
      case t@q"$something.get" if ! t.parent.exists( isApply ) =>
        Result(message(t),t)
    }
  }

  private[this] def message(tree: Tree) = Message("Usage of get on optional type.")

  private[this] def isApply(tree: Tree): Boolean = tree match{
    case q"$expr(..$aexprs)" => true
    case q"$expr[..$tpesnel]" => true
    case _ => false
  }
}