Functional programming encourages pipelining transformations of an immutable collection to shape it to its
desired result. This often leads to very succinct solutions, but can also be confusing to the reader - it is often difficult
to discern the author's intent, or keep track of all the intermediate results that are only implied. For example, let's say
we wanted to aggregate votes for different programming languages from a sequence of (language, num votes), showing them
in order of most votes to least, we could write:

    val votes = Seq(("scala", 1), ("java", 4), ("scala", 10), ("scala", 1), ("python", 10))
    val orderedVotes = votes
        .groupBy(_._1)
        .map { case (which, counts) =>
           (which, counts.foldLeft(0)(_ + _._2))
         }.toSeq
            .sortBy(_._2)
            .reverse

this is both succinct and correct, but nearly every reader will have a difficult time recovering the original intent of
the author. A strategy that often serves to clarify is to name intermediate results and parameters:

    val votesByLang = votes groupBy { case (lang, _) => lang }
    val sumByLang = votesByLang map { case (lang, counts) =>
        val countsOnly = counts map { case (_, count) => count }
        (lang, countsOnly.sum)
    }
    val orderedVotes = sumByLang.toSeq
        .sortBy { case (_, count) => count }
        .reverse

[Effective Scala](https://twitter.github.io/effectivescala/#Collections-Style)
