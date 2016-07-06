package codacy.patterns

import codacy.base.{Pattern, Result}

import scala.meta._

case object MultipleStringLiteralsChecker extends Pattern{

  override def apply(tree: Tree): Iterable[Result] = {
    tree.collect{ case lit@Lit(value:String) => tupled(lit,value) }.
      groupBy{ case (l,_) => l }.
      mapValues{ _.map{ case (_,tree) => tree }.distinct }.
      collect{ case (strings, tree :: others) if others.nonEmpty =>
        Result(message(strings,others.length + 1),tree)
      }
  }

  private[this] def tupled(lit:Lit,value:String) = {
    lit.parent match{
      case Some(t@Term.Interpolate(_,parts,_)) =>
        (parts.collect{ case Lit(s:String) => s }.toList, t)
      case _ =>
        (List(value),lit)
    }
  }

  private[this] def message(lits:List[String], numOccurences:Int) = {
    val value = lits.mkString("${ .. }")
    Message(s"The string literal '${value}' appears $numOccurences times in the file.")
  }
}