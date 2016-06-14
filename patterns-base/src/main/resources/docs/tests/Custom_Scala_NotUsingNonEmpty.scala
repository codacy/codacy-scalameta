//#Patterns: Custom_Scala_NotUsingNonEmpty
package docs.tests

class LengthOnly {
    def length = 0
    def size = 0
}

object Foo{
    
    def setCount() = {
        val arrays = Array(Array(1), Array(2))
        //#Warn: Custom_Scala_NotUsingNonEmpty
        arrays.map(_.length > 0)
        //#Warn: Custom_Scala_NotUsingNonEmpty
        arrays.map(_.size > 0)
        val seqs = Seq(Seq("a", "b"), Seq("c", "c"))
        //#Warn: Custom_Scala_NotUsingNonEmpty
        seqs.map { case item => item.length > 0}
        //#Warn: Custom_Scala_NotUsingNonEmpty
        seqs.map { case item => item.size > 0}
        //#Warn: Custom_Scala_NotUsingNonEmpty
        Seq("string", "string").map { item => item.length>0}

        // Unfortunately we're also detecting this occurence,
        // due to lack of type inference..
        val obj = new LengthOnly()
        //#Warn: Custom_Scala_NotUsingNonEmpty
        obj.length > 0
        //#Warn: Custom_Scala_NotUsingNonEmpty
        obj.size > 0
    }
}
