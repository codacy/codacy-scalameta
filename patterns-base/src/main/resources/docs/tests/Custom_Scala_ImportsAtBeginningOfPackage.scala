//#Patterns: Custom_Scala_ImportsAtBeginningOfPackage
package outer

import foo.bar.baz

package inner1{

  class Foo{
    //#Info: Custom_Scala_ImportsAtBeginningOfPackage
    import foo.bar.baz2
  }
}
package inner2{

trait Foo{
}
}

class Bar{
  def bar: Boolean
}

trait ImportCtx{
  def foo:String

  def bar:Bar
}

class Inner(importValue:ImportCtx) {
  import importValue._

  def defWithImport(importCtx:ImportCtx) = {
    import importCtx.bar._
    println("body")
  }

}