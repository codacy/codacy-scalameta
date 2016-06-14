package docs.tests

object Foo{

  def bar(i:Int) = ??? //todo: replace with a better name

  /*
  * FIXME: please implement
  * also: this is a multiline comment
  * */
  def bar2(i:Int) = ???

  /**
    this is a javadoc like comment with a lowercase fixme
    */
  val baz = 99

}

//#Patterns: Custom_Scala_BadTagPresenceCheck
//#Issue: {"severity": "Info", "line": 5,  "patternId": "Custom_Scala_BadTagPresenceCheck"}
//#Issue: {"severity": "Info", "line": 7,  "patternId": "Custom_Scala_BadTagPresenceCheck"}
//#Issue: {"severity": "Info", "line": 13, "patternId": "Custom_Scala_BadTagPresenceCheck"}
