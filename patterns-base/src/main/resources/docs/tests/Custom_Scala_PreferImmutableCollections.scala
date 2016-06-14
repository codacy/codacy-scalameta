//#Patterns: Custom_Scala_PreferImmutableCollections
package test1{
  //#Info: Custom_Scala_PreferImmutableCollections
  import scala.collection.mutable.Set
  
  object Foo{
    val t = Set
  }

}
package test2{
  //#Info: Custom_Scala_PreferImmutableCollections
  import scala.collection.mutable

  object Foo{
    //#Info: Custom_Scala_PreferImmutableCollections
    val t = mutable.Buffer

  }
}

package test3{

  object Foo{
    //#Info: Custom_Scala_PreferImmutableCollections
    val b = scala.collection.mutable.Buffer

  }

}
