package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ProhibitObjectName extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t: Defn.Class =>
        defsWithNameIn(t, t.name.toString)
      case t @ q"..$mods trait $tname[..$tparams] extends $template" =>
        defsWithNameIn(t, tname.toString)
      case t @ q"..$mods object $name extends $template" =>
        defsWithNameIn(t, name.toString)

    }.flatten
  }

  private[this] def defsWithNameIn(tree: Tree, cName: String) = {
    tree.collect {
      case t @ q"..$mods def $name[..$tparams](...$paramss): $tpeopt = $expr" if name.toString.endsWith(cName) =>
        Result(message(t), t)
      case t @ q"..$mods def $name[..$tparams](...$paramss): $tpe" if name.toString.endsWith(cName) =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("You should not name methods after their defining object")
}
