Pattern matching should always be exhaustive:

Bad:

    something match {
      case Success(v) => other
    }

Good:

    something match {
      case Success(v) => other
      case _ => another
    }

[More Info](https://www.scala-lang.org/files/archive/spec/2.11/08-pattern-matching.html)
