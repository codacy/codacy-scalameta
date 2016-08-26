package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._

case object Custom_Scala_UselessComparison extends Pattern {

  override def apply(tree: Tree): List[Result] = {
    tree.collect {
      case t@q"${lhs: Lit} $_ ${rhs: Lit}" if lhs.value == rhs.value =>
        Result(message(lhs.toString), t)
      case t@q"${lhs: Term.Name} $_ ${rhs: Term.Name}" if lhs.value == rhs.value =>
        Result(message(lhs.value), t)
    }
  }

  private[this] def message(expr: String) =
    Message(s"Useless comparison of $expr")

}
