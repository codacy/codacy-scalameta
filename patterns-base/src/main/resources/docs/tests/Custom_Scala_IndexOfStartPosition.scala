//#Patterns: Custom_Scala_IndexOfStartPosition
package docs.tests

class IndexOfStartPos {

  val s = "One String"

  //#Info: Custom_Scala_IndexOfStartPosition
  s.indexOf("String")

  //#Info: Custom_Scala_IndexOfStartPosition
  "xpto".indexOf("String")

  // correct case
  s.indexOf("String", 0)

  // case that should not be caught
  s.split("x")
}