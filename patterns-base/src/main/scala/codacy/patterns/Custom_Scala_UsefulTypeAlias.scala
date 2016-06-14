package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_UsefulTypeAlias extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{ case t@q"..$mods type $tname[..$tparams] = $tpe" if isDummyAlias(tpe) =>
      Result(message(t),t)
    }
  }

  private[this] def isDummyAlias(tree:Tree): Boolean = {
    tree match{
      case t"(..$atpes) => $tpe" =>
        atpes.length == 1 && atpes.headOption.exists(isPrimitive) && isPrimitive(tpe)
      case _ => false
    }
  }

  private[this] def isPrimitive(tpe: Tree):Boolean = {
    primitiveTypes.contains(tpe.toString)
  }

  private[this] lazy val primitiveTypes = Set("Byte", "Short", "Int", "Long", "Float", "Double", "Boolean", "String", "Char")

  private[this] def message(numImports: Tree) = Message(s"Don't use self-explanatory types aliases")
}