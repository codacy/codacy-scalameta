package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_EnforceMinimumVisibility extends Pattern {

  override def apply(tree: Tree): Iterable[Result] = tree.collect {
    case t: Defn if t.mods.exists(isPrivate) && isInner(t) && !isAccessedInAccompanied(t) =>
      Result(message(name(t)), t)
  }

  private[this] def isInner(tree: Defn) = tree match {
    case t @ (_: Defn.Trait | _: Defn.Class | _: Defn.Object) =>
      t.parent.exists {
        case q"package $_ { ..$_ }" => false
        case _ => true
      }
    case _ => true
  }

  private[this] def name(defn: Defn) = defn match {
    case d: Member => d.name.syntax
    case d: Defn.Val => d.pats.map(_.syntax).mkString(",")
    case d: Defn.Var => d.pats.map(_.syntax).mkString(",")
    case other => other.syntax
  }

  private[this] def isPrivate(mod: Mod) = mod match {
    case mod"private" => true
    case _ => false
  }

  private[this] def message(name: String) = Message(s"${name} should have a smaller scope by using private[this]")

  //poor man's approach ...
  private[this] def isAccessedInAccompanied(defn: Defn) = {
    Option(defn)
      .collect {
        case d: Defn.Def => Set(d.name)
        case d: Defn.Val => d.collect { case n: Name => n }.to[Set]
        case d: Defn.Var => d.collect { case n: Name => n }.to[Set]
      }
      .exists(
        defNames =>
          defn.parent.flatMap(_.parent).exists {
            case companion: Defn.Object =>
              lazy val rawNames = defNames.map(_.syntax)
              companion.parent.map(_.children).getOrElse(Seq.empty).exists {
                case accompanied: Defn.Class if companion.name.syntax == accompanied.name.syntax =>
                  accompanied.templ
                    .collect {
                      case q"$expr.$name" if expr.syntax == companion.name.syntax && rawNames.contains(name.syntax) =>
                        true
                    }
                    .exists(identity)
                case _ => false
              }
            case _ => false
          }
      )
  }

  private[this] implicit class DefExtensions(val defn: Defn) {

    def mods: Seq[Mod] = defn match {
      case d: Defn.Class => d.mods
      case d: Defn.Def => d.mods
      case d: Defn.Trait => d.mods
      case d: Defn.Object => d.mods
      case d: Defn.Type => d.mods
      case d: Defn.Val => d.mods
      case d: Defn.Var => d.mods
      case d: Defn.Macro => d.mods
      case _ => Seq.empty
    }
  }
}
