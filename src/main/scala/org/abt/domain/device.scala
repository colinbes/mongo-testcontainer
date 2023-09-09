package org.abt.domain

import org.joda.time.DateTime
import org.mongodb.scala.bson.ObjectId

import java.util.Date

object device {
  final case class Device(
      _id: ObjectId,
      name: String,
      polled: DateTime
  )
}
