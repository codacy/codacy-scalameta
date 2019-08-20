//#Patterns: Custom_Scala_EmptyForBlock
package docs.tests

class EmptyForBlock {
  for(a <- 1 to 3)
  //#Warn: Custom_Scala_EmptyForBlock
  {}

  for{a <- 1 to 5
    if a != 0}
  //#Warn: Custom_Scala_EmptyForBlock
  yield {}

  def testFine(): Boolean = {
    for(a <- 1 to 3)
    {
      true
    }

    for{a <- 1 to 5
      if a != 0}
    yield {
      true
    }
  }
}