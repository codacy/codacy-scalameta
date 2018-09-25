package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_LowerCasePatternMatching extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect {
      case t@p"case ${pat: Pat.Var} => $expr" if isOffender(t, pat) =>
        Result(message(t),t)
    }    
  }

  private[this] def isOffender(tree: Tree, pat: Pat) = {
    !hasDeclaredType(pat) && !isCaseFromCollect(tree) && !isCaseFromPartialFunction(tree) && isLowerCase(pat)
  }

  private[this] def isPartialApplication(tpe: Term.Name) = tpe.toString match {
    case "orElse" | "andThen" => true
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
      case _ => false
    }
  }

  private[this] def isLowerCase(pat: Pat) = {
    pat match{
      case q"${name: Pat.Var }" =>
        name.name.value.headOption.exists(Character.isLowerCase)
      case _ =>
        false
    }
  }

  private[this] def hasDeclaredType(pat: Pat) = {
    pat.collect { case t@p"$expr: $tpe" => t}.nonEmpty
  }

  private[this] def message(tree: Tree) = Message("Lower case pattern matching.")
}