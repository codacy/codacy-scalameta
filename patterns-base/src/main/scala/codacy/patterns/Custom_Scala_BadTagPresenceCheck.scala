package codacy.patterns

import codacy.base.{Pattern, Result}
import scala.meta._

class Custom_Scala_BadTagPresenceCheck(config: Custom_Scala_BadTagPresenceCheck.Configuration) extends Pattern {

  def this() = this(Custom_Scala_BadTagPresenceCheck.Configuration())

  override def apply(tree: Tree): Set[Result] = {
    tree.tokens.collect {
      case comment: Token.Comment if hasFixme(comment.value) =>
        Result(Message("Take the required action to fix the issue indicated by this comment."), comment)
    }.toSet
  }

  private[this] lazy val fixmeTags = {
    if (config.caseSensitive) config.fixmeTags else config.fixmeTags.map(_.toLowerCase)
  }

  private[this] def hasFixme(comment: String) = {
    val content = if (config.caseSensitive) comment else comment.toLowerCase
    fixmeTags.exists(content.contains)
  }
}

case object Custom_Scala_BadTagPresenceCheck {
  case class Configuration(caseSensitive: Boolean = false, fixmeTags: Set[String] = Set("FixMe", "ToDo"))
}
