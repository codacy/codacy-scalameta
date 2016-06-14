//#Patterns: Custom_Scala_IndexOfPositive
package docs.tests

object IndexOfCompTest {

  val color = "blue"
  val name = "ishmael"

  val strings = List(color, name)

  //#Info: Custom_Scala_IndexOfPositive
  if (strings.indexOf(color) > 0) {
    // Noncompliant
  }

  //#Info: Custom_Scala_IndexOfPositive
  if (0 < strings.indexOf(color)) {
    // Noncompliant
  }

  //#Info: Custom_Scala_IndexOfPositive
  if (name.indexOf("ish") > 0) {
    // Noncompliant
  }

  //#Info: Custom_Scala_IndexOfPositive
  if (0 < name.indexOf("ish")) {
    // Noncompliant
  }

  //#Info: Custom_Scala_IndexOfPositive
  if (name.indexOf("hma") > 2) {
    // Noncompliant
  }

  //#Info: Custom_Scala_IndexOfPositive
  if (2 < name.indexOf("hma")) {
    // Noncompliant
  }

  if (strings.indexOf(color) > -1) {
    // ...
  }

  if (-1 < strings.indexOf(color)) {
    // ...
  }

  if (name.indexOf("ish") >= 0) {
    // ...
  }

  if (0 <= name.indexOf("ish")) {
    // ...
  }

  if (name.indexOf("hma") > -1) {
    // ...
  }

  if (-1 < name.indexOf("hma")) {
    // ...
  }
}
