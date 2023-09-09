package org.abt

import com.typesafe.config.ConfigFactory
import org.mongodb.scala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import org.abt.mongo.document.Document2Json.DocumentToJson
object Main {

  def main(args: Array[String]): Unit = {
//    val codecRegistry: CodecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(DateTimeCodec), MongoClient.DEFAULT_CODEC_REGISTRY)

    val config = ConfigFactory.load().getConfig("mongo")
    val url = config.getString("url")
    val settings = MongoClientSettings
      .builder()
      .applyConnectionString(new ConnectionString(url))
      .build()
    val client = MongoClient(settings)
    val database = client.getDatabase("bcmongo")

    val deviceCollection = database.getCollection("device")
    val collections = database
      .listCollectionNames()
      .toFuture()
      .recoverWith { case _: Throwable =>
        println("return false")
        Future.successful(List())
      }
      .map { seq =>
        if (seq.isEmpty) false else true
      }
    val res = Await.result(collections, 10.seconds)
    println(s"Is Up $res")

    val devices = Await.result(
      deviceCollection
        .find()
        .toFuture(),
      2.seconds
    )
    devices.foreach(item => println(item.asJson()))
    client.close()
  }
}
