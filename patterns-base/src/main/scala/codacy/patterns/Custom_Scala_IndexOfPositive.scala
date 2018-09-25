package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_IndexOfPositive extends Pattern {

  override def apply(tree: Tree): List[Result] = {
    tree.collect {
      case t@q"$_.indexOf( ..${args: Seq[Term]}) > ${lit: Lit}" if isNaturalNumber(lit) =>
        Result(message(t), t)
      case t@q"${lit: Lit} < $_.indexOf( ..${args: Seq[Term]})" if isNaturalNumber(lit) =>
        Result(message(t), t)
    }
  }

  private def isNaturalNumber(lit: Lit): Boolean = { // return true if n >= 0
    lit.value match {
      case n : Number => n.doubleValue() >= 0
      case _ => false // should never happen. if it is a literal, and not a number, then it would be a string/bool. then > should not compile
    }
  }

  private[this] def message(tree: Tree) = Message("indexOf checks should be for negative numbers")
}