package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_PatternMatchingWhenDefaults extends Pattern{
  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"$_ match { ..case $casesnel }" if isMatchingHead(casesnel) =>
        Result(message,t)
    }
  }

  private[this] def isMatchingHead(cases:Seq[Tree]):Boolean = {
    cases.size == 2 && cases.exists{
      case p"case $expr :: _ if $guard => $_" =>
        true
      case _ => false
    }
  }

  private[this] lazy val message = Message("Consider using headOption")
}
