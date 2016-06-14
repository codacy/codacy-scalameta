//#Patterns: Custom_Scala_TypeParameterName
package docs.tests

//#Issue: {"severity": "Warn", "line": 14, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 14, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 16, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 16, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 16, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 23, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 23, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 23, "patternId": "Custom_Scala_TypeParameterName"}
//#Issue: {"severity": "Warn", "line": 28, "patternId": "Custom_Scala_TypeParameterName"}

case class Entry[TKey, TProperties](key: TKey, properties: TProperties)

trait HistoryBuilder[TKey, TEntity <: Option[TProperties], TProperties]

class Response[A]

trait TResponse[T]

trait a {
  def HistoryEncoder[TKey, TEntity <: Option[Int], TProperties]()
}

case class Cat[T, B](key: T, properties: B)

trait Dog[F, G <: Option[String], Milk]
