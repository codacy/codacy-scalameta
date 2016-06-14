//#Patterns: Custom_Scala_GroupImports
package docs.tests

//#Info: Custom_Scala_GroupImports
import foo.bar.Baz
import foo.bar.Baz2
//#Info: Custom_Scala_GroupImports
import foo.buzz.Baz
import foo.buzz.Baz2
import foo.buzz.Baz3
import scala.util.Random

//already grouped:
import foo.baz.{Bar1,Bar2}

import scala.math.BigDecimal
import java.util.UUID

object MyObject {

  def foo(i: Int) = i.toString

  def foo2(i: Int) = i.toString.reverse

}