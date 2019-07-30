//#Patterns: Custom_Scala_DuplicatedCase
package docs.tests

class DuplicatedCase {
  List(1,2,3) match {
    //#Warn: Custom_Scala_DuplicatedCase
    case 1 => println("Hello")
    case 1 => println(" World!")
    case _ => println("Hello World!")
  }
}