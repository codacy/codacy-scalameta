//#Patterns: Custom_Scala_ExplicitMutableNamespace
package test1{
  //#Info: Custom_Scala_ExplicitMutableNamespace
   import scala.collection.mutable.{Buffer, Set}
    
    object Bar{
        
        val set = Set()
        
        val buffer = Buffer()
        
    }
    
}

package test2{
   import scala.collection.mutable
    
    object Bar{
        
        val listBuffer = mutable.ListBuffer()
        
    }
    
}

package test3{
    //#Info: Custom_Scala_ExplicitMutableNamespace
    import scala.collection.mutable._

    object Foo{
      val listBuffer = ListBuffer()
    }
}