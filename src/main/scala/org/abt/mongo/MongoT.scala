package org.abt.mongo

import com.mongodb.{ServerApi, ServerApiVersion}
import com.typesafe.config.Config
import org.abt.DateTimeCodec
import org.abt.domain.device.Device
import org.bson.codecs.configuration.{CodecRegistries, CodecRegistry}
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.mongodb.scala.{ConnectionString, Document, MongoClient, MongoClientSettings, MongoCollection, MongoDatabase, Observable}
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.slf4j.Logger

import scala.annotation.unused
import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

object MongoT {
  protected[mongo] var databaseName: Option[String] = None
  protected[mongo] var databaseHost: Option[String] = None
  var mongoClient: Option[MongoClient] = None
  protected[mongo] var database: Option[MongoDatabase] = None

  private val customCodecs = fromProviders(classOf[Device])
  private val codecRegistry: CodecRegistry =
    CodecRegistries.fromRegistries(customCodecs, CodecRegistries.fromCodecs(DateTimeCodec), MongoClient.DEFAULT_CODEC_REGISTRY)

  private def getMongoDB(uri: String): MongoClient = {
    val serverApi = ServerApi.builder.version(ServerApiVersion.V1).build()
    lazy val settings = MongoClientSettings
      .builder()
      .applyConnectionString(ConnectionString(uri))
      .serverApi(serverApi)
      .build()
    MongoClient(settings)
  }

  def init(config: Config)(implicit ec: ExecutionContext, logger: Logger): Unit = {
    val url = config.getString("url")
    val name = config.getString("database")
    val host = config.getString("host")
    logger.info(s"mongo client connection url $url")
    val client = getMongoDB(url)
    mongoClient = Some(client)
    val db = client.getDatabase(name).withCodecRegistry(codecRegistry)
    databaseName = Some(name)
    databaseHost = Some(host)
    database = Some(db)
  }

  def close(): Unit = {
    mongoClient match {
      case Some(mc) =>
        mc.close()
        mongoClient = None
      case _ => throw new Exception("database not open")
    }
  }

  def listCollections(): Observable[String] = {
    database match {
      case Some(db) => db.listCollectionNames()
      case _        => throw ConnectionException("No database found")
    }
  }

  def createCollection(collectionName: String) = {
    database match {
      case Some(db) => db.createCollection(collectionName)
      case _ =>
        throw ConnectionException("Database not setup")
    }
  }
  def getCollection[T: ClassTag](collectionName: String): MongoCollection[T] = {
    database match {
      case Some(db) => db.getCollection[T](collectionName)
      case _ =>
        throw ConnectionException("Database not setup")
    }
  }

  @unused
  def getDatabaseName: String = {
    database match {
      case Some(db) => db.name
      case _        => throw ConnectionException("Database not setup")
    }
  }
}
