//#Patterns: Custom_Scala_NonFatal
package docs.tests

import java.util.NoSuchElementException

import scala.util.control.NonFatal

class nonFatal1 {

  def A() {
    try {
      throw new NoSuchElementException
    }
    catch {
      case e: NoSuchElementException => None

      case e if NonFatal(e) => println("catch me!")
    }
  }

  def B() {
    try {
      throw new NoSuchElementException
    }
    catch {
      //#Warn: Custom_Scala_NonFatal
      case e: Throwable => println("catch me!")
    }

    try {
      throw new NoSuchElementException
    }
    catch {
      //#Warn: Custom_Scala_NonFatal
      case _ =>  println("catch me!")
    }

    try {
      throw new NoSuchElementException
    }
    catch {
      //#Warn: Custom_Scala_NonFatal
      case e:java.lang.Exception =>  println("catch me!")
    }
  }

  def C() {
    try {
      throw new NoSuchElementException
    }
    catch {
      //#Warn: Custom_Scala_NonFatal
      case t: Throwable => println("catch me!")
    }
  }

  class HigherLevelException extends Exception

  def D() {
    try {
      throw new NoSuchElementException
    }
    catch {
      case e: HigherLevelException => None
      case NonFatal(_) => None
    }
  }

  def E() {
    try {
      throw new NoSuchElementException
    }
    catch {
      //#Warn: Custom_Scala_NonFatal
      case e => println("catch me!")
    }
  }
}
