//#Patterns: Custom_Scala_FieldNamesChecker
package docs.tests

trait Foo[A]{

    //false positives detected by scalastyle:
    val (t1,t2,t3) = ("",0,"")
    //enum styles
    object E1 extends Enumeration{
        val A,B,C = Value
    }

    //#Info: Custom_Scala_FieldNamesChecker
    val A = 0

    //#Info: Custom_Scala_FieldNamesChecker
    var B = 1

    //#Info: Custom_Scala_FieldNamesChecker
    val C:String

    //#Info: Custom_Scala_FieldNamesChecker
    var D:Int

    //#Info: Custom_Scala_FieldNamesChecker
    class Foo1(A:String)

    //#Info: Custom_Scala_FieldNamesChecker
    class Foo2(val A:String)

    //#Info: Custom_Scala_FieldNamesChecker
    class Foo3(var A:String)

    //#Info: Custom_Scala_FieldNamesChecker
    class Foo4(a:String)(implicit A:A)

    //Don't emit about this.
    val FilePatternRegex = """test""".r
    val FilePatternRegex1 = "a".r

    class ObjectWithRMethod {
        def r = 42
    }

    val obj = new ObjectWithRMethod()
    //#Info: Custom_Scala_FieldNamesChecker
    val NotRegex = obj.r

    val `angular-local-storage` = ???

    val /*weird comment*/`Angular-local-storage` /*another weird comment*/ = ???
}
