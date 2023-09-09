package com.abt.tests.device

import com.abt.tests.fixtures.DeviceFixtures
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.typesafe.config.{Config, ConfigFactory}
import org.abt.domain.device.Device
import org.abt.json.Json4sFormat
import org.abt.mongo.MongoT
import org.json4s.jackson.Serialization.{read, write}
import org.mongodb.scala.WriteConcern
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.slf4j.{Logger, LoggerFactory}
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DeviceCodecTest
    extends AnyFlatSpec
    with TestContainerForAll
    with BeforeAndAfterAll
    with Json4sFormat
    with Matchers
    with OptionValues
    with DeviceFixtures {

  implicit val logger: Logger = LoggerFactory.getLogger("ProgramRestTest")
  val mongoConfig: Config = ConfigFactory.load().getConfig("mongo1")
  private val domain: String = mongoConfig.getString("host")
  assert(domain.equals("localhost"), s"Invalid domain $domain found for test container database")

  def wait[C](obs: Future[C], delay: FiniteDuration = 2.seconds): C = Await.result(obs, delay)
  val conf: Config = ConfigFactory.load()

  override def beforeAll(): Unit = {}

  override def afterAll(): Unit = {}

  "DockerComposeContainer" should "be up" in withContainers { composedContainers =>
    assert(composedContainers.getServicePort("mongo", 27017) > 0)
    assert(composedContainers.getServicePort("redis", 6379) > 0)
  }

  "Create collection" should "create database" in {
    val newCollection = "tst-device"
    wait(MongoT.createCollection(newCollection).toFuture())
    val collections = wait(MongoT.listCollections().toFuture()).toList
    collections.length must equal(1)
    collections.head must equal(newCollection)
  }

  "Codec based Write/Read Devices" should "have equal strings" in {
    val list = deviceList
    val devices = read[List[Device]](list)
    devices.length must equal(9)
    val deviceCollection = MongoT.getCollection[Device]("tst-device")
    devices.foreach(device => {
      wait(deviceCollection.withWriteConcern(WriteConcern.ACKNOWLEDGED).insertOne(device).toFuture())
    })
    val readDevices: List[Device] = wait(
      deviceCollection
        .find()
        .toFuture()
        .map(seq => seq.toList)
    )
    readDevices.length must equal(9)
    val json = write(readDevices)
    val tst = deviceList.toLowerCase().equals(json.toLowerCase())
    tst must equal(true)
  }

  override val containerDef: DockerComposeContainer.Def = DockerComposeContainer.Def(
    new File("src/test/resources/docker-compose.yml"),
    tailChildContainers = true,
    exposedServices = Seq(
      ExposedService("redis", 6379),
      ExposedService("mongo", 27017, Wait.forLogMessage(".*Waiting for connections.*", 2))
    )
  )

  override def afterContainersStart(containers: DockerComposeContainer): Unit = {
    super.afterContainersStart(containers) // doesn't look like calling super does anything.
    MongoT.init(mongoConfig)
  }
  override def beforeContainersStop(containers: DockerComposeContainer): Unit = {
    super.beforeContainersStop(containers)
    MongoT.close()
  }
}
