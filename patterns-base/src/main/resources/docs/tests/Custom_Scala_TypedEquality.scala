//#Patterns: Custom_Scala_TypedEquality
package docs.tests

object Test extends Enumeration {
  val A, B = Value
}

class Custom_Scala_TypedEquality {
  //#Err: Custom_Scala_TypedEquality
  2 == "3"

  //#Err: Custom_Scala_TypedEquality
  2.==("3")

  //#Err: Custom_Scala_TypedEquality
  new Object eq 3

  //#Err: Custom_Scala_TypedEquality
  (new Object).eq(3)

  //#Err: Custom_Scala_TypedEquality
  Test.A == 4

  //#Err: Custom_Scala_TypedEquality
  2 != "3"

  //#Err: Custom_Scala_TypedEquality
  2.!=("3")

  //#Err: Custom_Scala_TypedEquality
  new Object ne 3

  //#Err: Custom_Scala_TypedEquality
  (new Object).ne(3)

  //#Err: Custom_Scala_TypedEquality
  2 equals 3

  //#Err: Custom_Scala_TypedEquality
  (new Object).equals(3)

  Test.A === 4

  Test.A =!= 4
}
