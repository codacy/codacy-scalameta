package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_TypedEquality extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t@q"$lexpr == $rexpr" =>
        Result(message(t), t)
      case t@q"$expr.==(..$aexprs)" =>
        Result(message(t), t)

      case t@q"$lexpr != $rexpr" =>
        Result(message(t), t)
      case t@q"$expr.!=(..$aexprs)" =>
        Result(message(t), t)

      case t@q"$lexpr eq $rexpr" =>
        Result(message(t), t)
      case t@q"$expr.eq(..$aexprs)" =>
        Result(message(t), t)

      case t@q"$lexpr ne $rexpr" =>
        Result(message(t), t)
      case t@q"$expr.ne(..$aexprs)" =>
        Result(message(t), t)

      case t@q"$lexpr equals $rexpr" =>
        Result(message(t), t)
      case t@q"$expr.equals(..$aexprs)" =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("Usage of untyped comparison operator")
}
