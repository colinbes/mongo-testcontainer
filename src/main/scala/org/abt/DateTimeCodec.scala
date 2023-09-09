package org.abt

import org.bson.codecs.Codec
import org.joda.time.{DateTime, DateTimeZone}

object DateTimeCodec extends Codec[DateTime] {
  def encode(writer: org.bson.BsonWriter, value: org.joda.time.DateTime, encoderContext: org.bson.codecs.EncoderContext): Unit = {
    writer.writeDateTime(value.getMillis)
  }

  def decode(reader: org.bson.BsonReader, decoderContext: org.bson.codecs.DecoderContext): org.joda.time.DateTime = {
    val millis = reader.readDateTime()
    new DateTime(millis, DateTimeZone.UTC)
  }

  def getEncoderClass: Class[org.joda.time.DateTime] = classOf[DateTime]
}
