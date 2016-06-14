package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_UsingDateTimeNow extends Pattern{

  override def apply(tree: Tree) = {
    tree.collect{
      case t@q"DateTime.now" if ! t.parent.exists(timeZoneGiven) =>
        Result(message(t),t)
    }
  }

  private[this] def timeZoneGiven(tree: Tree): Boolean = tree match {
    case q"DateTime.now($timezone)" => true
    case _ => false
  }

  private[this] def message(tree: Tree) = Message("Usage of DateTime.now.")
}