The collections libraries usually provide a method for determining if the iterable has at least one element, with

     val hasElements = SomeSequence.nonEmpty

This is a bit cleaner than verifying explicitly the length or the size

     val hasElements = SomeSequence.length > 0
     val hasElementsUsingSize = SomeSequence.size > 0
