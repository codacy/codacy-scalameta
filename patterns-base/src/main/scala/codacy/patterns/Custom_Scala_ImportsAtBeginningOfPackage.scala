package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ImportsAtBeginningOfPackage extends Pattern {

  override def apply(tree: Tree): Iterable[Result] = {
    tree
      .collect {
        case q"package $_ { ..$stats }" =>
          filterPackages(stats)
            .dropWhile(isImport)
            .flatMap(_.collect {
              case t: Tree if isNonLocalParamImport(t) =>
                Result(message(t), t)
            })
      }
      .flatten
      .to[Set]
  }

  private[this] def filterPackages(stats: Seq[Stat]): Seq[Stat] = {
    stats.headOption.collect { case q"package $_ { ..$block }" => filterPackages(block) }.getOrElse(stats)
  }

  private[this] def importsLocalValue(term: Term.Name): Boolean = {
    def findParentParamLists(term: Tree): Seq[Term.Param] = {
      term.parent match {
        case Some(d: Defn.Def) => d.paramss.flatten
        case Some(c: Defn.Class) => c.ctor.paramss.flatten
        case other => other.map(findParentParamLists).getOrElse(Seq.empty)
      }
    }

    findParentParamLists(term).exists(_.name.syntax == term.syntax)
  }

  private[this] def isImport(tree: Tree): Boolean = tree match {
    case q"import ..$_" => true
    case _ => false
  }

  private[this] def rootName(term: Term): Option[Term.Name] = term match {
    case tn: Term.Name => Some(tn)
    case Term.Select(term, _) => rootName(term)
    case _ => None
  }

  private[this] def isNonLocalParamImport(tree: Tree): Boolean = tree match {
    case importer"$ref.{..$_}" => !rootName(ref).exists(importsLocalValue)
    case _ => false
  }

  private[this] def message(tree: Tree) = Message("Put imports in the beginning of the package")
}
