package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_GetInMethodName extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"..$mods def ${name: Term.Name}[..$tparams](...$paramss): $tpeopt = $expr"
          if name.value != "get" && name.value.startsWith("get") =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("Do not prefix getters with 'get'")
}
