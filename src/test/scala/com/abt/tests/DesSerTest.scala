package com.abt.tests
import com.abt.tests.domain.{DeviceUpdates, MyTimestamp, UpdateModule}
import org.abt.helpers.DateTimeUtils
import org.abt.json.Json4sFormat
import org.abt.mongo.document.Document2Json.DocumentToJson
import org.joda.time.DateTime
import org.json4s.jackson.Serialization.write
import org.mongodb.scala.bson.ObjectId
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/** Validate that DateTime is serialized and deserialized maintaining 3 digit milliseconds and that DateTime is serialized to
  * form {$date: ISO date string}
  */
class DesSerTest extends AnyFlatSpec with BeforeAndAfterAll with Json4sFormat with Matchers with OptionValues {
  import org.mongodb.scala.Document
  val logger: Logger = LoggerFactory.getLogger("ProgramRestTest")
  def wait[C](obs: Future[C], delay: FiniteDuration = 2.seconds): C = Await.result(obs, delay)

  override def beforeAll(): Unit = {}
  override def afterAll(): Unit = {}

  "Deserialize TS with fraction" should "show fraction" in {
    val info = DateTime.parse("2023-09-06T14:30:12.123Z")
    val updateDevice = DeviceUpdates(DateTimeUtils.newUTCDateTime)
    val updateModule = UpdateModule(Some(updateDevice))
    val ts = MyTimestamp(new ObjectId(), "something", info, Some(updateModule))
    val json0: String = write(ts)
    val doc = Document.apply(json0)
    val json1: String = doc.asJson()
    val tst = json0.equals(json1.replace(" ", ""))
    tst must equal(true)
  }

  "Deserialize TS with zeroed fraction" should "show zeroed fraction" in {
    val info = DateTime.parse("2023-09-06T14:30:12.000Z")
    val updateDevice = DeviceUpdates(info)
    val updateModule = UpdateModule(Some(updateDevice))
    val ts = MyTimestamp(new ObjectId(), "something", info, Some(updateModule))
    val json0: String = write(ts)
    val doc = Document.apply(json0)
    val json1: String = doc.asJson()
    val tst = json0.equals(json1.replace(" ", ""))
    tst must equal(true)
  }
}
