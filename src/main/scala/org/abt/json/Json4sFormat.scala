package org.abt.json

import org.abt.helpers.DateTimeUtils.{DateTime2String, String2DateTime}
import org.bson.types.ObjectId
import org.joda.time.{DateTime, DateTimeZone}
import org.json4s.JsonAST._
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, Formats, MappingException, Serializer, TypeInfo}

trait Json4sFormat {
  implicit val formats: Formats = DefaultFormats + new MyObjectIdSerializer + new MyDateTimeSerializer
  implicit val serialization: Serialization.type = Serialization
}

object Json4sFormat extends Json4sFormat

/** Class to handle serialization of Mongo ObjectId to Mongo format.
  */
class MyObjectIdSerializer extends Serializer[ObjectId] {
  private val ObjectIdClass = classOf[ObjectId]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), ObjectId] = {
    case (TypeInfo(ObjectIdClass, _), json) =>
      json match {
        case JObject(JField("$oid", JString(s)) :: Nil) => new ObjectId(s)
        case JString(s)                                 => new ObjectId(s)
        case x                                          => throw new MappingException("Can't convert " + x + " to  ObjectId")
      }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = { case x: ObjectId =>
    JObject(JField("$oid", JString(x.toString)) :: Nil)
  }
}

/** Class to handle datetime conversions to Mongo Date {$$date : ISODate} format using UTC timezone.
  */
class MyDateTimeSerializer extends Serializer[DateTime] {
  private val DateTimeClass = classOf[DateTime]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DateTime] = {
    case (TypeInfo(DateTimeClass, _), json) =>
      json match {
        case JObject(JField("$date", JString(s)) :: Nil) => s.toDateTime
        case JObject(JField("$date", JInt(i)) :: Nil)    => new DateTime(i.longValue, DateTimeZone.UTC)
        case JString(dateText)                           => dateText.toDateTime
        case x => throw new MappingException("Can't convert " + x + " to  DateTime")
      }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = { case x: DateTime =>
    JObject(JField("$date", JString(x.asString(true))) :: Nil)
  }
}
