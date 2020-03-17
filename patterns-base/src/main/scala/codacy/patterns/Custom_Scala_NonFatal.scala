package codacy.patterns

import codacy.base.Pattern

import scala.meta._
import scala.util.control.ControlThrowable

case object Custom_Scala_NonFatal extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t @ q"try $expr catch { ..case $cases } finally $expropt" =>
        cases.collect {
          case cs if isNonFatalCatch(cs) =>
            Result(message(cs), cs)
        }
    }.flatten
  }

  //should we mind the guards??
  //it's currently not catching case f:_ => ...
  private[this] def isNonFatalCatch(caseDef: Tree): Boolean = {
    caseDef match {
      case t @ p"case $pat if $expropt => $_" =>
        val patternTest = pat match {
          case p"$appl($e)" => false
          case p"$_: $tpt" => isNonFatal(tpt)
          case p"_" => true
          case p"$_" => true

          //bind... don't know what to do here
          case p"$pname @ $apat" => false
          case _ => false
        }
        val guardTest = expropt match {
          case Some(q"NonFatal(..$args)") => false
          case _ => true
        }
        patternTest && guardTest
      case _ => false
    }
  }

  private[this] def isNonFatal(tree: Tree): Boolean = {
    allEvilSymbols.contains(tree.toString())
  }

  private[this] def message(tree: Tree) = Message("Fatal exception is being caught")

  private[this] lazy val vmErr = reflect.runtime.universe.typeOf[VirtualMachineError]
  private[this] lazy val thrErr = reflect.runtime.universe.typeOf[ThreadDeath]
  private[this] lazy val intErr = reflect.runtime.universe.typeOf[InterruptedException]
  private[this] lazy val linErr = reflect.runtime.universe.typeOf[LinkageError]
  private[this] lazy val ctrlErr = reflect.runtime.universe.typeOf[ControlThrowable]
  private[this] lazy val genErr = reflect.runtime.universe.typeOf[Throwable]
  private[this] lazy val genErr2 = reflect.runtime.universe.typeOf[Exception]

  private[this] lazy val allEvilSymbols = Set(vmErr, thrErr, intErr, linErr, ctrlErr, genErr, genErr2).flatMap {
    case tpe =>
      val sym = tpe.typeSymbol
      Set(sym.fullName, sym.name.decodedName.toString)
  }
}
