//#Patterns: Custom_Scala_NameResultsAndParameters
package docs.tests

class UnnamedParameters {
  val votes = Seq(("scala", 1), ("java", 4), ("scala", 10), ("scala", 1), ("python", 10))

  val orderedVotes = votes
    //#Warn: Custom_Scala_NameResultsAndParameters
    .groupBy(_._1)
    .map { case (which, counts) =>
    //#Warn: Custom_Scala_NameResultsAndParameters
    (which, counts.foldLeft(0)(_ + _._2))
  }.toSeq
    //#Warn: Custom_Scala_NameResultsAndParameters
    .sortBy(_._2)
    .reverse

  val votesByLang = votes groupBy { case (lang, _) => lang}
  val sumByLang = votesByLang map { case (lang, counts) =>
    val countsOnly = counts map { case (_, count) => count}
    (lang, countsOnly.sum)
  }
  val fineOrderedVotes = sumByLang.toSeq
    .sortBy { case (_, count) => count}
    .reverse

  val (awesomeLanguages, otherLanguages) = votes.partition { case (lang, _) => lang == "scala"}
}
