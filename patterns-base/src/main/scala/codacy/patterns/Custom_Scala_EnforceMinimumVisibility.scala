package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_EnforceMinimumVisibility extends Pattern{


  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"..$mods val ..$patsnel: $tpeopt = $expr" if mods.exists(isPrivate) =>
        Result(message(patsnel.toString),t)
      case t@q"..$mods var ..$patsnel: $tpeopt = $expropt" if mods.exists(isPrivate) =>
        Result(message(patsnel.toString),t)
      case t@q"..$mods def $name[..$tparams](...$paramss): $tpeopt = $expr" if mods.exists(isPrivate) =>
        Result(message(name.toString),t)
      case t:Defn.Class if t.mods.exists(isPrivate) && isInner(t) =>
        Result(message(t.name.toString),t)
      case t@q"..$mods trait $tname[..$tparams] extends $template" if mods.exists(isPrivate) && isInner(t) =>
        Result(message(tname.toString),t)
      case t@q"..$mods object $name extends $template" if mods.exists(isPrivate) && isInner(t) =>
        Result(message(name.toString),t)
      case t@q"..$mods type $tname[..$tparams] = $tpe" if mods.exists(isPrivate) =>
        Result(message(tname.toString),t)
    }
  }

  private[this] def isInner(tree:Tree) = {
    tree.parent.exists{
      case q"package $ref { ..$stats }" => false
      case _ => true
    }
  }

  private[this] def isPrivate(mod:Mod) = mod match{
    case mod"private" => true
    case _ => false
  }

  private[this] def message(name: String) = Message(s"${name} should have a smaller scope by using private[this]")
}
