object Test {
  def exampleOption(o: Option[Int]): Unit = {
    o match {
      case Some(i) => println(i)
    }
  }
  def exampleEither(o: Either[Int, String]): Unit = {
    o match {
      case Left(i) => println(i)
    }
  }
  def exampleTry(o: Try[String]): Unit = {
    o match {
      case Success(i) => println(i)
    }
  }

  sealed trait CustomTrait
  case object Option1 extends CustomTrait
  case object Option2 extends CustomTrait

  def example2(c: CustomTrait): Unit = c match {
    case Option1 => println("option 1")
    case Option2 => println("option 2")
  }

  def negativeExampleOption(o: Option[Int]) = o match {
    case Some(1) => println("got 1")
    case None => println("got None")
  }
}
