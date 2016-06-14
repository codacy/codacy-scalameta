//#Patterns: Custom_Scala_EnforceMinimumVisibility
package docs.tests

import docs.{deactivatedTests, tests}

class Foo(foo: deactivatedTests.Foo) {
    self =>

  private[this] val selfAccessible: deactivatedTests.Foo = foo

  //#Warn: Custom_Scala_EnforceMinimumVisibility
  private val typeAccesible: deactivatedTests.Foo = foo

  //#Warn: Custom_Scala_EnforceMinimumVisibility
  private class OpenClass(openness: Int)

  //#Warn: Custom_Scala_EnforceMinimumVisibility
  private object OpenObject { self =>

    //#Warn: Custom_Scala_EnforceMinimumVisibility
    private def openness(diameter: Long) = {
      Math.pow(diameter, diameter)
    }
  }

}
