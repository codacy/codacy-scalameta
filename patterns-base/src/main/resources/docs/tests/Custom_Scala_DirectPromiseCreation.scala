//#Patterns: Custom_Scala_DirectPromiseCreation
package docs.tests

object Foo{

    //#Info: Custom_Scala_DirectPromiseCreation
    val promise = Promise[Int]()

    //#Info: Custom_Scala_DirectPromiseCreation
    val nothing = Promise.apply()

    //#Info: Custom_Scala_DirectPromiseCreation
    val failed = Promise.failed(???)

    //#Info: Custom_Scala_DirectPromiseCreation
    val success = Promise.successful(???)

    //#Info: Custom_Scala_DirectPromiseCreation
    val fromTry = Promise.fromTry(???)
    
}