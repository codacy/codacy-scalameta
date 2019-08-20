package codacy.base

import scala.meta.{Position, Token, Tree}

trait Positionable extends Any {
  def pos: Position
}

object Positionable {

  implicit class TreePositionable(val tree: Tree) extends AnyVal with Positionable {
    override def pos: Position = tree.pos
  }

  implicit class TokenPositionable(val token: Token) extends AnyVal with Positionable {
    override def pos: Position = token.pos
  }
}
