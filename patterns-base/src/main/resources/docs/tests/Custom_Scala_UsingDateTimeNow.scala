//#Patterns: Custom_Scala_UsingDateTimeNow
package docs.tests

import org.joda.time.{DateTimeZone, DateTime}

class DateTimeNow {
    //#Warn: Custom_Scala_UsingDateTimeNow
    val now1 = DateTime.now
    //#Warn: Custom_Scala_UsingDateTimeNow
    val now2 = DateTime.now()

    // Don't warn for this, since it's correctly generating an UTC aware datetime object.
    val now3 = DateTime.now(DateTimeZone.UTC)
}
