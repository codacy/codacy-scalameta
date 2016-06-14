//#Patterns: Custom_Scala_LowerCasePatternMatching
package docs.tests

object Foo{
  
  val P = "good"
  val p = "bad"
  
  val test = 24

  test match {
      case P => "works"
      case `P` => "still works"
      case p: Any => "good"
      case p: Int => "okay"
      case Some(p) => "okay"
      case "p" => "great"
      case p if p > 24 => 42
      case p if p < 34 => 24
      //#Warn: Custom_Scala_LowerCasePatternMatching
      case p => "no good"
      case _ => "good!"
  }

  // These are fine.

  Option("test").collect { case some => some }

  case class Test(x: Int, y: Int)

  Test(1, 2) match{ 
    case obj@Test(z,y) => obj
  }

  private val ala: PartialFunction[Int, Int] = { case i:Int => i + i }
  type PF = PartialFunction[Any, Int]
  val pf1: PF =
      { case nana: Int => nana }
  val pf2: PF ={case n: Int => n}
  //OK
  private val bala: PartialFunction[Int, Int] = ala orElse { case i => i + 42 }
  //OK
  private val portocala: PartialFunction[Int, Int] = bala andThen { case i => i + 24}
  //OK
  private val another: PartialFunction[Int, Int] = portocala.andThen{ case i => i + 42}
  //OK
  private val func: PartialFunction[Int, Int] = another.orElse{ case i if i > 1 => i + 24}
  private val func1: PartialFunction[Int, Int] = func.orElse[Int, Int]{ case i if i > 1 => i + 24}
  private val func2: PartialFunction[Int, Int] = func1.andThen[Int]{ case i if i > 1 => i + 24}

  Seq().foldLeft(10) {
    case (offset, _) if offset > 10 => offset
    case _ => 1
  }


}
