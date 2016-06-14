//#Patterns: Custom_Scala_GetCalls
package docs.tests

class Get {

  val optional1: Option[Int] = Option(1)
  val optional2 = Option(1)
  val optional3 = None

  //#Warn: Custom_Scala_GetCalls
  optional1.get
  //#Warn: Custom_Scala_GetCalls
  optional2.get
  //#Warn: Custom_Scala_GetCalls
  optional3.get

  optional1.getOrElse(2)
  optional2.getOrElse(3)
  optional3.getOrElse(4)

  //#Warn: Custom_Scala_GetCalls
  Option(1).get

  //#Warn: Custom_Scala_GetCalls
  Option.empty[String].map(identity).get

  //#Warn: Custom_Scala_GetCalls
  None.get

  //#Warn: Custom_Scala_GetCalls
  Some(322).get

  Option(423423).map(_ + 342).getOrElse(432)

  //#Warn: Custom_Scala_GetCalls
  Right(34).left.map(identity).right.get

  //previously false positives:
  object Bla{
    def get[T](int:Int=0):T = ???
    def get[T]:T = get[T](0)

  }

  Bla.get[Int](352)
  Bla.get[String]

}
