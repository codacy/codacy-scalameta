//#Patterns: Custom_Scala_JavaThreads
package docs.tests

object Foo{

  //#Info: Custom_Scala_JavaThreads
  val thread = new Thread()

  val runnable: Runnable = ???
  //#Info: Custom_Scala_JavaThreads
  val threadR = new Thread(runnable)

}