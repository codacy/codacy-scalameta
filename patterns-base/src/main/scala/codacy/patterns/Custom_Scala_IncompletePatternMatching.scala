package codacy.patterns

import codacy.base.Pattern

import scala.meta._

object Custom_Scala_IncompletePatternMatching extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"$expr match { ..case $casesnel }" if isSingleMatchCase(casesnel) =>
        Result(message, t)
      case t @ q"$_.$fkt{ ..case $casesnel }" if functions.contains(fkt.toString) && isSingleMatchCase(casesnel) =>
        Result(message, t)
    }.toSet
  }

  private[this] def isSingleMatchCase(cases: Seq[Tree]): Boolean = {
    cases.toList match {
      case p"case $pat if $guard => $expr" :: Nil => guard.nonEmpty
      case _ => false
    }
  }

  private[this] lazy val functions = Set("map")

  private[this] lazy val message = Message("incomplete pattern matching")
}
