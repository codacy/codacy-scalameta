//#Patterns: Custom_Scala_ImportsSortedAlphabetically

import foo.bar.baz
//#Info: Custom_Scala_ImportsSortedAlphabetically
import boo.far.baz

class Foo{

  import foo.bar.baz
  //#Info: Custom_Scala_ImportsSortedAlphabetically
  import boo.far.baz

  val foo = 0

  import foo.bar.baz
  //#Info: Custom_Scala_ImportsSortedAlphabetically
  import boo.far.baz

  //case sensitivity test
  import play.Logger
  import play.api.Play.current
  import play.api.mvc._

}
