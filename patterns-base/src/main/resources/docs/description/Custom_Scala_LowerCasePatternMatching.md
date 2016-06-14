A lower case pattern match clause with no other tokens is the same as _.
This is not true for patterns which start with an upper case letter. This can cause confusion, and may not be what was intended:

    val foo = "foo"
    val Bar = "bar"
    "bar" match { case Bar => "we got bar" }   // result = "we got bar"
    "bar" match { case foo => "we got foo" }   // result = "we got foo"
    "bar" match { case `foo` => "we got foo" } // result = MatchError

The second match is a potential problem, since it matches to Any.
To fix it, use an identifier which starts with an upper case letter, use case _ or, if you wish to refer to the value, add a type : Any.

    val lc = "lc"
    "something" match { case lc: Any => "lc" } // result = "lc"
    "something" match { case _ => "lc" } // result = "lc"

