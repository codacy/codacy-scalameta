//#Patterns: Custom_Scala_PatternMatchingWhenDefaults
package docs.tests

object Test{
  val list = List()
  val default = 1
  val xxx = 2

  //#Info: Custom_Scala_PatternMatchingWhenDefaults
  list match {
    case head :: _ => head
    case Nil => default
  }

  list match {
    case head :: other => xxx
    case head :: other :: _ => xxx
    case Nil => default
  }

  list match {
    case head :: _ => xxx
    case head :: other => xxx
    case Nil => default
  }
}