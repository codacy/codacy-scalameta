Because Scala's exception mechanism isn't checked, the compiler cannot statically tell whether the programmer has covered the set of possible exceptions, it is often tempting to cast a wide net when handling exceptions.
 However, some exceptions are fatal and should never be caught.

 For more details:

 [Scala Docs - NonFatal](http://www.scala-lang.org/api/current/scala/util/control/NonFatal$.html)

 [Effective Scala - Handling exceptions](http://twitter.github.io/effectivescala/)
