Pattern matching should always have a default case:

 Bad:

    something match {
        case a if false => other
    }

 Good:

    something match {
        case a if false => other
        case _ => another
    }
    
 [More Info](http://www.scala-lang.org/files/archive/spec/2.11/08-pattern-matching.html)