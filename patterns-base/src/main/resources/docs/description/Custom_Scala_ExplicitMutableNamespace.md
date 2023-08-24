Don't import scala.collection.mutable._ and refer to Set, instead,

     import scala.collection.mutable
     val set = mutable.Set()

 makes it clear that the mutable variant is being used.

 For more details:

 [Effective Scala](https://twitter.github.io/effectivescala/#Collections-Use)
