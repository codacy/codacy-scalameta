//#Patterns: Custom_Scala_EmptyIfBlock
package docs.tests

class EmptyIfBlock {
  //#Warn: Custom_Scala_EmptyIfBlock
  if(true)
  {}

  //#Warn: Custom_Scala_EmptyIfBlock
  if(false)
  {}
  else
  {}

  //#Warn: Custom_Scala_EmptyIfBlock
  if(true)
  {}
  //#Warn: Custom_Scala_EmptyIfBlock
  else if(false)
  {}
  else
  {}

  def testFine1(foobar: Boolean): Boolean = {
    if(foobar)
    {
      true
    }
    else
    {
      false
    }
  }

  def testFine2(foobar: Boolean): Boolean = {
    if(foobar)
    {
      true
    }
  }
}