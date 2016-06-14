Do not use structural types in normal use. They are a convenient and powerful feature, but unfortunately do not have an efficient implementation on the JVM. However — due to an implementation quirk — they provide a very nice shorthand for doing reflection.

 val obj: AnyRef

 obj.asInstanceOf[{def close()}].close()


 For more details:

 [Effective Scala](http://twitter.github.io/effectivescala/#Object%20oriented%20programming-Structural%20typing)