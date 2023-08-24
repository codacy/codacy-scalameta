Instead of using

     def suffix(i: Int) =
       if      (i == 1) "st"
       else if (i == 2) "nd"
       else if (i == 3) "rd"
       else             "th"

 prefer

     def suffix(i: Int) = i match {
       case 1 => "st"
       case 2 => "nd"
       case 3 => "rd"
       case _ => "th"
     }

 For more details:

 [Effective Scala](https://twitter.github.io/effectivescala/#Control%20structures-Returns)
