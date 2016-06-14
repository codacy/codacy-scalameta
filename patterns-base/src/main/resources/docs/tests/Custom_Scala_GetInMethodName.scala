//#Patterns: Custom_Scala_GetInMethodName

package docs.tests

class Foo{
    //#Info: Custom_Scala_GetInMethodName
    def getCount():Int = 77
}

trait Bar{
    //#Info: Custom_Scala_GetInMethodName
    def getDependency = new Dependency{}
}

object Baz{
    //#Info: Custom_Scala_GetInMethodName
    def getStaticThing():Option[String] = Option.empty

    def falseGetPositive = {}
}