//#Patterns: Custom_Scala_EmptyWhileBlock
package docs.tests

class EmptyWhileBlock {
  while(true)
  //#Warn: Custom_Scala_EmptyWhileBlock
  {}

  //#Warn: Custom_Scala_EmptyWhileBlock
  do {}
  while(true)

  def testFine1(foobar: Boolean): Boolean = {
    while(foobar)
    {
      true
    }
    false
  }

  def testFine2(foobar: Boolean): Boolean = {
    do
    {
      true
    }
    while(foobar)

    false
  }
}