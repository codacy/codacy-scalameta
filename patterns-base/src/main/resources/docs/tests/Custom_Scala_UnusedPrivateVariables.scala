//#Patterns: Custom_Scala_UnusedPrivateVariables

package docs.tests

//#Warn: Custom_Scala_UnusedPrivateVariables
class Foo(/*private variables in constructor*/ private[this] val foo:String, private val bar: String,
          private val companionAccess:String){

  //#Warn: Custom_Scala_UnusedPrivateVariables
  private[this] val (baz,baz2) = ("",3)

  //access a private inside a scope variable
  lazy val b = baz2 - 1

  //access a private inside a scope variable
  def useFoo() = {
    println(foo)
  }

  //shadowing via parameter
  def doesntUseBar(bar:String) = bar

  //shadowing via anonymous function parameter
  def alsoDoesntUseBar() = {
    Option("").map(bar => bar)// ++
    Option("").map{ case bar => bar } ++
    Option(("","")).map{ case bar@(t1,t2) => t1 }
  }

  def doesntUseBaz(baz:String) = baz

  def noBazHere = {
    val baz = 44
    baz
  }

  def innerDef() = {
    def baz = 33
    baz
  }

  def innerDefWithTypeP() = {
    def baz[A] = 33
    baz
  }

  def alsoDoesntUseBaz() = {
    Option(11).map(baz => baz.toString) ++
    Option(12).map{ case baz => baz.toString}
  }

  abstract class ShadowingBar(private val bar:String,
                              //#Warn: Custom_Scala_UnusedPrivateVariables
                              private val foo:String/*an inner class unused that is used in the outer*/){
    def myBar = bar
  }

  private val otherCompanionAccess = ""
}

object Foo{
  //companion that accesses a private value of the class
  def fromClass(instance:Foo) = instance.companionAccess

  def otherClassAccess(instance:Foo):String = instance.otherCompanionAccess
}

class Curried(private[this] val c1:String)(val c2:Int = c1.length)

//#Warn: Custom_Scala_UnusedPrivateVariables
class Foo2(private val a:String){
  def a[A]:Int = 11
  def usesDefA = a[String]
}

//TODO: The parameter a is not used but we don't detect it
class Foo3(private val a:String){
  def a[A](a:A):Int = 11
  def usesDefA = a("")
}

//TODO: The parameter a is not used but we don't detect it whereas b IS used (by bb)
class Foo4[A](private val a:A => Int, private val b:A => Int){
  def a[A](a:A):Int = 11
  def usesDefA = a("")

  def b[A](a:A):Int = 11
  def usesParamB(a:A) = b(a)
}

