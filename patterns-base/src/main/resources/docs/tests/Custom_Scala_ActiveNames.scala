//#Patterns: Custom_Scala_ActiveNames
package docs.tests

class Foo{

    //#Info: Custom_Scala_ActiveNames
    def setCount(i:Int) = {}
}

trait Bar{

    //#Info: Custom_Scala_ActiveNames
    def setDependency(d:Dependency) = {}
}

object Baz{

    //#Info: Custom_Scala_ActiveNames
    def setMeFree(b:Baz) = {}

    def falseSetPositive(s:S) = {}
}