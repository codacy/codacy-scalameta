//#Patterns: Custom_Scala_CallByNameAsLastArguments
package docs.tests

class Foo{
    def callByNameDef1(i:Int, s:String, byName: => String) =
        s"$byName - $s"

    //#Warn: Custom_Scala_CallByNameAsLastArguments
    def callByNameDef2(i:Int, byName: => String, s:String) =
        s"$byName - $s"

    def callByNameDef3(byName: => String) =
        s"$byName"

    def callByNameDef4(byName: => String, secondByName: => String) =
        s"$byName"

    //#Warn: Custom_Scala_CallByNameAsLastArguments
    def callByNameDef2(i:Int, byName: => String, s:String, byName2: => String) =
        s"$byName - $s"
}
