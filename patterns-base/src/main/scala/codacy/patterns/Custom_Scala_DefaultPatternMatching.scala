package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_DefaultPatternMatching extends Pattern {

  override def apply(tree: Tree): List[Result] =
    tree.collect {
      case Term.Match(_, cases: List[Case]) =>
        val allCases = cases.collect {
          case t @ p"case $pat if $guard => $expr" if isOffender(t) =>
            t
        }

        allCases.groupBy(_.parent).toList.collect {
          case (Some(parent), cases) if !hasDefaultCase(cases) =>
            Result(message, parent)
        }
    }.flatten

  private[this] def isOffender(tree: Tree) = {
    isPartialMatchForKnownTypes(tree)
  }

  private[this] def isPartialMatchForKnownTypes(tree: Tree): Boolean = {
    // Collect all the types from the cases and see if we have usages of Left/Right
    // and Success/Failure paired together.
    val types: Set[String] = tree.parent.toSet[Tree].flatMap {
      _.collect {
        case p"$pat: $tpe" => tpe.toString
        case p"${name: Term.Name}" => name.toString
        case p"$expr(..$pats)" => expr.toString
      }.toSet
    }

    val knownTypes = Seq(Set("Left", "Right"), Set("Failure", "Success"), Set("Some", "None"))

    knownTypes.exists { cases =>
      val intersection = types.intersect(cases)
      intersection.nonEmpty && intersection != cases
    }
  }

  private[this] def hasDefaultCase(cases: Seq[Tree]) = {
    cases.exists {
      case t @ p"case $pat if $guard => $_" if guard.isEmpty =>
        //check the pattern
        pat match {
          //Extract
          case p"$expr(..$pats)" => false
          //Typed
          case p"$pat: $ptpe" => false
          case p"$_" => true
        }

      case _ => false
    }
  }

  private[this] lazy val message = Message("Pattern match is not exhaustive.")
}
