//#Patterns: Custom_Scala_StructuralTypes
package docs.tests

abstract class Foo{

  //#Warn: Custom_Scala_StructuralTypes
  type BarType = AnyRef { def bar:String }

  def bar1:String

  //#Warn: Custom_Scala_StructuralTypes
  def t(ff:{ type Foo }) = ff.foo

  //previously detected as false positive
  def bar: Foo with Bar

  //#Warn: Custom_Scala_StructuralTypes
  def bar: NotStructural with Structural{ val o:A }

}
