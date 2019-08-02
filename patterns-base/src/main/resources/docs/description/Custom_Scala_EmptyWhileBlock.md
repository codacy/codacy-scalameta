Avoid using empty blocks on while statements

     while( ... )
     {} // <-- avoid this

Or

     do
     {} // <-- avoid this
	 while( ... )

 For more details:

 [Scala Docs](https://docs.scala-lang.org/overviews/quasiquotes/expression-details.html#while-and-do-while-loops)