package org.abt.helpers

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}

object DateTimeUtils {
  val dtFmt: DateTimeFormatter = ISODateTimeFormat.dateTime().withZoneUTC()
  val dtLocalFmt: DateTimeFormatter = ISODateTimeFormat.dateTime()
  val dtParser: DateTimeFormatter = ISODateTimeFormat.dateTimeParser().withZoneUTC()
  val defaultTimezone: DateTimeZone = DateTimeZone.UTC

  /** Parse ISODate formatted to YYYY-MM-DDTHH:MM:SS.sssZ
    */
  def parse(isoDate: String): DateTime = dtParser.withZone(defaultTimezone).parseDateTime(isoDate)

  def print(date: DateTime): String = dtFmt.withZone(defaultTimezone).print(date)

  def printLocal(date: DateTime): String = dtLocalFmt.withZone(DateTimeZone.getDefault).print(date)

  /** Return UTC based DateTime object
    *
    * @return
    *   DateTime
    */
  def newUTCDateTime: DateTime = DateTime.now(DateTimeZone.UTC)

  implicit class DateTime2String(date: DateTime) {
    def asString(utc: Boolean = true): String = if (utc) {
      print(date)
    } else {
      printLocal(date)
    }
  }

  implicit class String2DateTime(isoDate: String) {
    def toDateTime: DateTime = parse(isoDate)
  }
}
