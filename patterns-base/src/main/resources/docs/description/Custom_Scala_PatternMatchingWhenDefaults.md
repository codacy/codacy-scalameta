The collections libraries usually provide methods that return Options. Avoid using

     val x = list match { case head :: _ => head; case Nil => default }

 because

     val x = list.headOption getOrElse default

 is both shorter and communicates purpose.

 For more details:

 [Effective Scala](https://twitter.github.io//effectivescala/#Functional%20programming-Call%20by%20name)
