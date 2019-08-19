package codacy.base

import scala.meta.Tree

trait Pattern extends Any {
  def apply(tree: Tree): Iterable[Result]
  def Message = codacy.base.Result.Message.apply _
  def Result = codacy.base.Result.apply _
  type Result = codacy.base.Result
}
