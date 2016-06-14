package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._

case object Custom_Scala_DefaultPatternMatching extends Pattern{

  override def apply(tree: Tree) = {
    val allCases: Seq[Tree] = tree.collect{
      case t@p"case $pat if $guard => $expr" if isOffender(t) =>
        t
    }

    allCases.groupBy(_.parent).toList.collect{ case (Some(parent),cases) if ! hasDefaultCase(cases) =>
      Result(message,parent)
    }
  }

  private[this] def isOffender(tree: Tree) = {
    !isCaseFromCollect(tree) && !isCaseFromPartialFunction(tree) && !isCompleteTypeCase(tree)
  }

  private[this] def isCompleteTypeCase(tree: Tree): Boolean = {
    // Collect all the types from the cases and see if we have usages of Left/Right
    // and Success/Failure paired together.
    val types = tree.parent.map {
      case tree => tree.collect {
        case p"$pat: $tpe" => tpe.toString
        case p"${name: Term.Name}" => name.toString
      }.toSet
    }
    types.fold(false) {
      item => (Set("Left", "Right") subsetOf item) || (Set("Failure", "Success") subsetOf item)
    }
  }

  private[this] def isPartialApplication(tpe: Term.Name) = tpe.toString match {
    case "orElse" => true
    case "andThen" => true
    case _ => false
  }

  private[this] def isCaseFromPartialFunction(tree: Tree): Boolean = {
    tree.parent.flatMap(_.parent).exists{
      case q"..$mods val ..$patsnel: $tpeopt = { ..case $casesnel }" => true
      case q"$expr $tpe { ..case $casesnel}" if isPartialApplication(tpe) => true
      case q"$expr.$tpe { ..case $casesnel }" if isPartialApplication(tpe) => true
      case q"$expr $tpe[..$tpesnel] { ..case $casesnel }" if isPartialApplication(tpe) => true
      case q"$expr.$tpe[..$tpesnel] { ..case $casesnel }" if isPartialApplication(tpe) => true
      case _ => false
    }
  }

  private[this] def isCaseFromCollect(tree: Tree): Boolean = {
    tree.parent.flatMap(_.parent).exists{
      case q"$_.collect(..$_)" => true
      case q"$_.collect[..$_](..$_)" => true
      case q"$_ collect $_" => true
      case q"$_.collectFirst(..$_)" => true
      case q"$_.collectFirst[..$_](..$_)" => true
      case q"$_ collectFirst $_" => true
      case _ => false
    }
  }

  private[this] def hasDefaultCase(cases:Seq[Tree]) = {
    cases.exists( _ match{
      case t@p"case $pat if $guard => $_" if guard.isEmpty =>
        //check the pattern
        pat match{
          //Extract
          case t@p"$ref[..$tpes](..$apats)" => false
          //Typed
          case p"$pat: $ptpe" => false
          case _ => true
        }

      case _ => false
    })

  }

  private[this] lazy val message = Message("Default case not specified.")
}