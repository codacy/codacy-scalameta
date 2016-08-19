//#Patterns: Custom_Scala_UselessComparison
package docs.tests

object Foo {

  //#Info: Custom_Scala_UselessComparison
  3 == 3

  3 == 3L

  /////////

  val lhs = 3
  val rhs = 3

  //#Info: Custom_Scala_UselessComparison
  lhs == lhs
  lhs == rhs

  /////////

  case class Bar(value: Int)

  //Info: Custom_Scala_UselessComparison
  Bar(3) == Bar(3)

}
