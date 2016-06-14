//#Patterns: Custom_Scala_CollectionLastHead

package analysis.samples.scala

class HeadLast {

  val sequence: Seq[Int] = Seq(1, 2, 3)
  val emptySequence = Seq()

  //#Warn: Custom_Scala_CollectionLastHead
  sequence.head
  //#Warn: Custom_Scala_CollectionLastHead
  sequence.last
  //#Warn: Custom_Scala_CollectionLastHead
  emptySequence.head
  //#Warn: Custom_Scala_CollectionLastHead
  emptySequence.last

  sequence.headOption.getOrElse(2)
  sequence.lastOption.getOrElse(2)
  emptySequence.headOption.getOrElse(2)
  emptySequence.lastOption.getOrElse(2)

  //#Warn: Custom_Scala_CollectionLastHead
  Seq(125).head
  //#Warn: Custom_Scala_CollectionLastHead
  Seq(125).last

  //#Warn: Custom_Scala_CollectionLastHead
  Seq.empty[Int].map(identity).head
  //#Warn: Custom_Scala_CollectionLastHead
  Seq.empty[Int].map(identity).last

}
