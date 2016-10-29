//#Patterns: ObjectNamesChecker
package analysis.samples.scala.scalastyle

object Foobar {
  val foo = 1
}

//#Info: ObjectNamesChecker
object foobaz {

  //#Info: ObjectNamesChecker
  object barbar {
  }

}

package object foobarz {

  //#Info: ObjectNamesChecker
  object barbar {
  }

}
