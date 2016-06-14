//#Patterns: Custom_Scala_ProhibitObjectName
package docs.tests

abstract class User {

  //#Warn: Custom_Scala_ProhibitObjectName
  def getUser(id: Int): Option[User]
}

object User1 {

  //#Warn: Custom_Scala_ProhibitObjectName
  def getUser1(id: Int): Option[User]
}

class User2 {

  //#Warn: Custom_Scala_ProhibitObjectName
  def getUser2(id: Int): Option[User]
}

trait User3 {

  //#Warn: Custom_Scala_ProhibitObjectName
  def getUser3(id: Int): Option[User]
}

abstract class User4 {
  def get(id: Int): Option[User]
}

object TestCase {
  //Test case for FT-1254, verifying that nested names don't emit a warning.
  class Bar {}
  class Foo5 {
      def bar: Bar
  }
}