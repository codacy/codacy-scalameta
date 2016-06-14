//#Patterns: Custom_Scala_ReturnFinally
package docs.tests

import scala.util.control.NonFatal

class TryCatch {

  try {
    ""
  }
  catch {
    case e: Throwable => ""
  } finally {
    "asdsd"
  }

  def c: Boolean = {
    try (throw new Exception(""))
    catch {
      case _ =>
    }
    finally {
      //#Warn: Custom_Scala_ReturnFinally
      return false
    }
    "das"
    true
  }


  def b: Boolean = {
    return true
  }


  def a: Boolean = {
    try (throw new Exception(""))
    catch {
      case NonFatal(e) => ""
    }
    finally {
      //#Warn: Custom_Scala_ReturnFinally
      return false
    }
    true
  }
}