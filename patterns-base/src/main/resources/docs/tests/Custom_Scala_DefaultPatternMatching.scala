//#Patterns: Custom_Scala_DefaultPatternMatching
package docs.tests

case class ClassEx(a: Int, b: Int) extends Exception

class NoDefaultPatternMatching {

  val something = new Exception
  val someSeq = Seq(
    1,
    2,
    3,
    4,
    5,
  )

  //OK
  something match {
    case a if true =>
    case b if false =>
    case _ =>
  }

  //OK
  something match {
    case a if true =>
    case b if false =>
    case e =>
  }

  // FAIL
  //#Warn: Custom_Scala_DefaultPatternMatching
  something match {
    case _ if true =>
    case a if false =>
  }

  // FAIL
  //#Warn: Custom_Scala_DefaultPatternMatching
  something match {
    case a if true =>
  }

  // FAIL
  //#Warn: Custom_Scala_DefaultPatternMatching
  something match {
    case a if true =>
    case b if false =>
    case ClassEx(a, b) =>
  }

  // FAIL
  //#Warn: Custom_Scala_DefaultPatternMatching
  something match {
    case a if true =>
    case b if false =>
    case e: ClassEx =>
  }

  //OK
  Seq().foldLeft(10) {
    case (offset, _) if offset > 10 => offset
    case (offset, _) => offset + 1
  }

  //OK
  someSeq.collect {
    case a if a > 2 => a
  }

  //OK
  val res = someSeq collect {
    case b if b > 3 => b
  }

  //OK
  val resFirst = someSeq.collectFirst {
    case b if b > 3 => b
  }

  val resSecond = someSeq collectFirst {
    case b if b > 3 => b
  }

  private val ala: PartialFunction[Int, Int] = { case i:Int if i > 0 => i }
  type PF = PartialFunction[Any, Int]
  val pf1: PF =
      { case n: Int => n }
  val pf2: PF ={case n: Int => n}
  //OK
  private val bala: PartialFunction[Int, Int] = ala orElse { case i if i > 1 => i + 42 }
  //OK
  private val portocala: PartialFunction[Int, Int] = bala andThen { case i if i > 1 => i + 24}
  //OK
  private val another: PartialFunction[Int, Int] = portocala.andThen{ case i if i > 1 => i + 42}
  //OK
  private val func: PartialFunction[Int, Int] = another.orElse{ case i if i > 1 => i + 24}
  private val func1: PartialFunction[Int, Int] = func.orElse[Int, Int]{ case i if i > 1 => i + 24}
  private val func2: PartialFunction[Int, Int] = func1.andThen[Int]{ case i if i > 1 => i + 24}

  // These are okay.
  Try(1 / 0) match {
     case Success(value) => 1
     case Failure(value) => 0
  }
  Try(1 / 0) match {
     case s:Success => 1
     case f:Failure => 0
  }
  Try(1 / 0) match {
     case Success(_) => 1
     case Failure(_) => 0
  }
  val value: Either[String, Int] = Right(42)
  value match {
     case Left(_) => "bad"
     case Right(_) => "good"
  }
  value match {
     case Left(value) => "bad"
     case Right(value) => "good"
  }
  value match {
     case l:Left => "bad"
     case r:Right => "good"
  }

  value match {
     case l:Left => "bad"
     case _ => "good"
  }

  value match {
     case r:Right => "bad"
     case _ => "good"
  }

  value match {
     case Left(_) => "bad"
     case _ => "good"
  }

  value match {
     case Success(_) => "bad"
     case _ => "good"
  }

  // These are not okay.
  //#Warn: Custom_Scala_DefaultPatternMatching
  Try(1 / 0) match {
     case Success(value) => 1
  }
  //#Warn: Custom_Scala_DefaultPatternMatching
  Try(1 / 0) match {
     case f:Failure => 2
  }

}
