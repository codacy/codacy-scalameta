Pattern matching should not have single cases with conditions:

Bad:

    something match {
      case a if false => other
    }

Good:

    something match {
      case a if false => other
      case a if true => other
    }

Also acceptable:

    "somestring" match {
      case "somestring" =>
    }

[Scala Docs](https://docs.scala-lang.org/tutorials/tour/pattern-matching.html)
