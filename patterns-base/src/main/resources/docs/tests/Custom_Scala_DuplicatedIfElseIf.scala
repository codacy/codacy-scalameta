//#Patterns: Custom_Scala_DuplicatedIfElseIf
package docs.tests

class DuplicatedIfElseIf {
  var a = true
  var b = false

  //#Warn: Custom_Scala_DuplicatedIfElseIf
  if(a && b) ""
  else if(a && b) ""
  else ""
}