package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_GetInMethodName extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case defn: Defn.Def
          if defn.name.value != "get" && defn.name.value.startsWith("get") =>
        Result(message(defn), defn)
    }
  }

  private[this] def message(tree: Tree) = Message("Do not prefix getters with 'get'")
}
