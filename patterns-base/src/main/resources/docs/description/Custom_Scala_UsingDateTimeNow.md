Using DateTime.now without specifying a timezone can lead to error prone code with regard to
dealing with other timezones than those initially considered, as well as with daylight saving time.

For instance, the following example

    val now = DateTime.now

could easily be written as this

    val now = DateTime.now(DateTimeZone.UTC)
