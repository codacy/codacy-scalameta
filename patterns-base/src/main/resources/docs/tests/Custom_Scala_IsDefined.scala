//#Patterns: Custom_Scala_IsDefined
package docs.tests

class IsDefined {

  val optional1: Option[Int] = Option(1)
  val optional2 = Option(1)
  val optional3 = None

  //#Warn: Custom_Scala_IsDefined
  optional1.isDefined
  //#Warn: Custom_Scala_IsDefined
  optional2.isDefined
  //#Warn: Custom_Scala_IsDefined
  optional3.isDefined

  optional3.isDefinedValue

  //#Warn: Custom_Scala_IsDefined
  Option(1).isDefined

  //#Warn: Custom_Scala_IsDefined
  Option.empty[String].map(identity).isDefined

  //#Warn: Custom_Scala_IsDefined
  None.isDefined

  //#Warn: Custom_Scala_IsDefined
  Some(322).isDefined

  //previously false positives:
  object Bla{
    def isDefined[T](int:Int=0):T = ???
    def isDefined[T]:T = get[T](0)

  }

  Bla.isDefined[Int](352)
  Bla.isDefined[String]

}
