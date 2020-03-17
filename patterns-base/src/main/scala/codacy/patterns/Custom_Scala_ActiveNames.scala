package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ActiveNames extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      //case q"..$mods def $name[..$tparams](...$paramss): $tpeopt = $expr"
      case t: Defn.Def if t.name.toString().startsWith("set") =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("Use active names for operations with side effects")
}
