package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_GetCalls extends Pattern {

  override def apply(tree: Tree) = {

    tree.collect {
      case t @ Term.Select(a, Term.Name("get")) if isValid(t) =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("Usage of get on optional type.")

  private[this] def isValid(t: Tree) = t.parent match {
    case Some(_: Term.ApplyType) => false
    case Some(Term.Apply((`t`, _))) => false
    case _ => true
  }
}
