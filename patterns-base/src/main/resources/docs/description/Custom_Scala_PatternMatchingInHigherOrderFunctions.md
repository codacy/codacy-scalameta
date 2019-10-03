Instead of

     list map { item =>
       item match {
         case Some(x) => x
         case None => default
       }
     }

 collapse the match

     list map {
       case Some(x) => x
       case None => default
     }

 For more details:

 [Effective Scala](https://twitter-archive.github.io/effectivescala/#Formatting-Pattern%20matching)
