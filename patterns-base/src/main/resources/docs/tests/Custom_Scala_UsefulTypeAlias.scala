//#Patterns: Custom_Scala_UsefulTypeAlias
package docs.tests

case class Foo( intMaker : String => Int ){

  //#Warn: Custom_Scala_UsefulTypeAlias
  type IntMaker = String => Int

  //#Warn: Custom_Scala_UsefulTypeAlias
  type DoubleMaker = Byte => Double
}