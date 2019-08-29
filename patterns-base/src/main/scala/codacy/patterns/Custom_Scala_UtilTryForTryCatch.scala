package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_UtilTryForTryCatch extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"try $_ catch { ..case $expr } finally $_" if expr.nonEmpty =>
        Result(message(t), t)
      case t @ q"try $_ catch $_ finally $_" =>
        Result(message(t), t)
    }
  }

  private[this] def message(name: Tree) = Message(s"Consider using util.Try instead of a try-catch block")
}
