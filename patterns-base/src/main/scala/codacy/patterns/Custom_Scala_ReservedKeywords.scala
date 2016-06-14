package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_ReservedKeywords extends Pattern {
  override def apply(tree: Tree) = {
    tree.collect{
      case t:Member if reserved.contains(t.name.toString) =>
        Result(message(t.name),t)
    }
  }

  private[this] val reserved = {
    (scala.reflect.runtime.universe match{ case table: scala.reflect.internal.SymbolTable =>
      table.nme.keywords.map(_.decodedName.toString)
    }).map{ case raw => s"""`$raw`"""}
  }

  private[this] def message(name: Name) = Message(s"$name is a reserved keyword don't use it as a name")
}