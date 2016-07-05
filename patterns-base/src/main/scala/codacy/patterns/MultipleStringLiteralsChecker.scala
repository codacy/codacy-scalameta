package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._

case object MultipleStringLiteralsChecker extends Pattern{

  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{ case l@Lit(s:String) => (l,s) }.groupBy{ case (_,value) => value }.collect{
      case (_, (lit,value) :: others) if others.nonEmpty =>
        Result(message(value,others.length + 1),lit)
    }
  }

  private[this] def message(value:String, numOccurences:Int) = {
    Message(s"The string literal '${value}' appears $numOccurences times in the file.")
  }
}