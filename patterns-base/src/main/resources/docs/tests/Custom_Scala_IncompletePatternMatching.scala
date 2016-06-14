//#Patterns: Custom_Scala_IncompletePatternMatching
package analysis.samples.scala

class IncompletePatternMatching {

  //#Warn: Custom_Scala_IncompletePatternMatching
  Seq(1, 2, 3).map {
    case e if e == 0 =>
      "zero"
  }

  //#Warn: Custom_Scala_IncompletePatternMatching
  "this is a string" match {
    case s if s.isEmpty => 0
  }

  Seq(1, 2, 3).collect {
    case e if e == 0 =>
      "zero"
  }

  //this should be a match maybe
  Seq().foldLeft(10) {
    case (offset, _) if offset > 10 => offset
  }

  Seq().foldLeft(10) {
    case (offset, _) if offset > 10 => offset
    case (offset, _) => offset + 1
  }

}
