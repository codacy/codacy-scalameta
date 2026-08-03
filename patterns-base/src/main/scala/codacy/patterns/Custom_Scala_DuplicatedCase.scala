package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_DuplicatedCase extends Pattern {
  override def apply(tree: Tree): Iterable[Result] = {
    def checkDup(cases: Seq[Case]): Iterable[Result] = {
      cases.groupBy(_.pat.structure).collect {
        case (x, ys) if ys.lengthCompare(1) > 0 =>
          Result(message(ys.head), ys.head)
      }
    }

    tree.collect {
      case expr: Term.Match =>
        checkDup(expr.cases)
      case expr: Term.Try =>
        checkDup(expr.catchp)
      case block: Term.Block =>
        block.stats.flatMap {
          case cases: Term.CasesBlock => checkDup(cases.cases)
          case _ => Iterable.empty
        }
    }.flatten
  }

  private[this] def message(tree: Tree) = Message("Duplicated case statements")
}
