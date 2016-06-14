//#Patterns: Custom_Scala_PatternMatchingInHigherOrderFunctions
package docs.tests

object Test{

  //#Info: Custom_Scala_PatternMatchingInHigherOrderFunctions
  List().map { item =>
    item match {
      case "" =>
      case Some(x) => x
      case None => 1
    }
  }

  //The correct version
  List().map {
    case "" =>
    case Some(x) => x
    case None => 1
  }

}