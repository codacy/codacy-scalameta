package codacy.patterns

import codacy.base.Pattern

import scala.meta.Term.Arg
import scala.meta._
import scala.meta.internal.{ast => impl}

case object Custom_Scala_IndexOfStartPosition extends Pattern {

  override def apply(tree: Tree) = {
    tree.collect {
      case t@q"$expr.indexOf( ..${args: Seq[Arg]} )" if args.length == 1 =>
        Result(message(t), t)
    }
  }

  private[this] def message(tree: Tree) = Message("indexOf checks should use a start position")
}
