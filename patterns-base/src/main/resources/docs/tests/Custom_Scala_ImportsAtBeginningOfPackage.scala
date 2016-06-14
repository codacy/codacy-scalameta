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
