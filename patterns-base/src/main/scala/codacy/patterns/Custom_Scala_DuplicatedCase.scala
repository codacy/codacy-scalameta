package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_DuplicatedCase extends Pattern {
override def apply(tree: Tree): Iterable[Result] = {
    def checkDup(cases: List[Case]): Iterable[Result] = {
      cases.groupBy(_.pat.structure).collect {
        case (x, ys) if ys.lengthCompare(1) > 0 =>
          Result(message(ys.head),ys.head)
      }
    }

    tree.collect{
      case q"$_ match { ..case $cases }" => checkDup(cases)
      case q"try $_ catch { ..case $cases } finally $_" => checkDup(cases)
      case q"{ ..case $cases }" => checkDup(cases)
    }.flatten
  }

  private[this] def message(tree: Tree) = Message("Duplicated case statements")
}