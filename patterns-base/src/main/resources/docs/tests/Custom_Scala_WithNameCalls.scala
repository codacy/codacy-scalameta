//#Patterns: Custom_Scala_WithNameCalls
package docs.tests

object Currency extends Enumeration {
   val GBP = Value("GBP")
   val EUR = Value("EUR")
}

class WithName {

  val currency = Currency
  val str = "EUR"

  //#Warn: Custom_Scala_WithNameCalls
  Currency.withName("EUR")
  //#Warn: Custom_Scala_WithNameCalls
  Currency.withName(str)

  //#Warn: Custom_Scala_WithNameCalls
  currency.withName("EUR")
  //#Warn: Custom_Scala_WithNameCalls
  currency.withName(str)

  Currency.values.find(_.toString == "EUR")

}
