Should always have only one case matching the same thing:

 Bad:

     List(1,2,3) match {
       case 1 => println("Hello")
       case 1 => println(" World!")
       case _ => println("Hello World!")
     }

 Good:

     List(1,2,3) match {
       case 1 => println("Hello")
       case _ => println("Hello World!")
     }

[More info](https://docs.scala-lang.org/tour/pattern-matching.html)
