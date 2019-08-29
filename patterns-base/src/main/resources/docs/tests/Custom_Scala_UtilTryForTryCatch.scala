//#Patterns: Custom_Scala_UtilTryForTryCatch
package docs.tests

class TryCatch {
    //#Info: Custom_Scala_UtilTryForTryCatch
    try{
       ""
    }
    catch{
        case e => ""
    }

    //#Info: Custom_Scala_UtilTryForTryCatch
    try(throw new Exception(""))
    catch{ case NonFatal(e) => "" }

    try{
      ""
    } finally {
      ""
    }

}
