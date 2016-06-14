package codacy.base

import scala.meta.Position

case class Result(message:Result.Message, private val pos:Positionable){
  lazy val position: Position = pos.pos
}

object Result{
  case class Message(value:String) extends AnyVal
}