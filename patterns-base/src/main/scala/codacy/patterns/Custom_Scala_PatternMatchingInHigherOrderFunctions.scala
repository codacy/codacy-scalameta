package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_PatternMatchingInHigherOrderFunctions extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"$sel.map(..$aexprssnel)" if isPatternMatch(aexprssnel) =>
        Result(message, t)
    }
  }

  private[this] def isPatternMatch(expr: Seq[Tree]): Boolean = {
    expr.toList match {
      case first :: Nil =>
        first match {
          //block
          case t @ q"{ (..$params) => $expr match { ..case $casesnel } }" =>
            true
          case q"(..$params) => $expr match { ..case $casesnel } " =>
            true
          case _ =>
            false
        }
      case _ => false
    }
  }

  private[this] lazy val message = Message("Use pattern matching directly in function definitions")
}
