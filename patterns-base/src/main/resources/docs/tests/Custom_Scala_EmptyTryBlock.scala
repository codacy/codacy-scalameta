//#Patterns: Custom_Scala_EmptyTryBlock
package docs.tests

class EmptyTryBlock {
  
  //#Warn: Custom_Scala_EmptyTryBlock
  try {}
  catch() {}

  //#Warn: Custom_Scala_EmptyTryBlock
  try {}
  catch() {
    println("Hello")
  }

  //#Warn: Custom_Scala_EmptyTryBlock
  try {
    println("Ohh no!")
  }
  catch() {}

  def nonEmpty(): Boolean = {
    try {
      println("Ohh no!")
    }
    catch() {
      true
    }
    false
  }
}