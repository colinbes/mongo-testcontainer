package org.abt.mongo.document

import org.bson.json.{Converter, JsonMode, JsonWriterSettings, StrictJsonWriter}
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.mongodb.scala.Document

import java.lang.{Long => LongJ}

class DTC extends Converter[LongJ] {
  private val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime().withZoneUTC()

  override def convert(value: LongJ, writer: StrictJsonWriter): Unit = {
    val str = fmt.print(value)
    writer.writeStartObject()
    writer.writeName("$date")
    writer.writeString(str)
    writer.writeEndObject()
//    writer.writeString(str)
  }
}

object Document2Json {
  val settings: JsonWriterSettings = JsonWriterSettings
    .builder()
    .outputMode(JsonMode.RELAXED)
    .dateTimeConverter(new DTC)
    .build()

  implicit class DocumentToJson(document: Document) {
    def asJson(): String = {
      document.toJson(settings)
    }
  }
}
