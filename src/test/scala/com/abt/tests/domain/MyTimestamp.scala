package com.abt.tests.domain

import org.joda.time.DateTime
import org.mongodb.scala.bson.ObjectId

case class MyTimestamp(id: ObjectId, name: String, ts: DateTime, updates: Option[UpdateModule])
