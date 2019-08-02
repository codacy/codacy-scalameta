//#Patterns: Custom_Scala_EmptyMethodBlock
package docs.tests

class EmptyMethodBlock {
  
  //#Warn: Custom_Scala_EmptyMethodBlock
  def empty(): Boolean = {}

  def nonEmpty(): Boolean = {
	  true
  }
}