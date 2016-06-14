//#Patterns: Custom_Scala_WildcardImportOnMany
package docs.tests

//#Warn: Custom_Scala_WildcardImportOnMany
import foo.bar.{Baz0,Baz1,Baz2,Baz3,Baz4,Baz5,Baz6}


object MyObject{

    def foo(i:Int) = i.toString

    def foo2(i:Int) = i.toString.reverse

}