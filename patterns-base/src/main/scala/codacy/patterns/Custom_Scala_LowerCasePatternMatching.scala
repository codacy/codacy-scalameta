package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_LowerCasePatternMatching extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ Case(pat: Pat.Var, _, _) if isOffender(t, pat) =>
        Result(message(t), t)
    }
  }

  private[this] def isOffender(tree: Tree, pat: Pat) = {
    !hasDeclaredType(pat) &&
    !isCaseFromCollect(tree) &&
    !isCaseFromPartialFunction(tree) &&
    !isCaseFromCatch(tree) &&
    isLowerCase(pat)
  }


  private[this] def isCaseFromPartialFunction(tree: Tree): Boolean = {
    tree.parent.flatMap(_.parent).exists {
      case _: Defn.Val => true
      case block: Term.Block =>
        block.stats.exists {
          case _: Term.CasesBlock => true
          case _ => false
        }
      case _ => false
    }
  }

  private[this] def isCaseFromCollect(tree: Tree): Boolean = {
    tree.parent.flatMap(_.parent).exists {
      case Term.Apply(Term.Select(_, Term.Name("collect")), _) => true
      case _ => false
    }
  }

  private[this] def isLowerCase(pat: Pat) = {
    pat match {
      case q"${name: Pat.Var}" =>
        name.name.value.headOption.exists(Character.isLowerCase)
      case _ =>
        false
    }
  }

  private[this] def hasDeclaredType(pat: Pat) = {
    pat.collect { case t @ p"$expr: $tpe" => t }.nonEmpty
  }

  private[this] def isCaseFromCatch(tree: Tree): Boolean = {
  tree.parent.exists {
    case t: Term.Try => t.catchp.exists(_ == tree)
    case _           => false
  }
}

  private[this] def message(tree: Tree) = Message("Lower case pattern matching.")
}
