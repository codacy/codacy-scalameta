package codacy.patterns

import codacy.base.Pattern

import scala.meta._

class Custom_Scala_ReservedKeywords(config:Custom_Scala_ReservedKeywords.Configuration) extends Pattern {

  def this() = this(Custom_Scala_ReservedKeywords.Configuration())

  override def apply(tree: Tree) = {
    tree.collect{
      case m:Member if reserved.contains(m.name.toString) && m.name.toString.nonEmpty =>
        Result(message(m.name),m)
    }
  }

  private[this] lazy val reserved = {
    (scala.reflect.runtime.universe match{ case table: scala.reflect.internal.SymbolTable =>
      table.nme.keywords.map(_.decodedName.toString)
    }).filterNot( config.exclude.contains ).map{ case keyWord => s"""`$keyWord`"""}
  }

  private[this] def message(name: Name) = Message(s"$name is a reserved keyword don't use it as a name")
}

object Custom_Scala_ReservedKeywords{
  case class Configuration(exclude:Set[String]=Set.empty)
}