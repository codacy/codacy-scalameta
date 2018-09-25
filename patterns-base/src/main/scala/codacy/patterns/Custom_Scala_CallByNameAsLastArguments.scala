package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_CallByNameAsLastArguments extends Pattern {

  override def apply(tree: Tree): List[Result] = {
    tree.collect{
      case t:Defn.Def if t.paramss.exists( isOffender ) =>
        Result(message(t.name),t)
    }
  }

  private[this] def isByNameParam(param:Term.Param):Boolean = {
    // i need a context, how to get one? param.isByNameParam
    param.collect{ case t"=> $tpe" => true }.exists(identity)
  }

  private[this] def isOffender(params:Iterable[Term.Param]):Boolean = {
    //check if we find a call-for-name followed by a non-call-by-name

    params.dropWhile{ param => ! isByNameParam(param) }.
      dropWhile( isByNameParam ).
      nonEmpty
  }

  private[this] def message(name: Name) = Message("Call-by-name parameter should be in last position")
}
