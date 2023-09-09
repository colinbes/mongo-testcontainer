ThisBuild / version := "0.2.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"
//fork := true
//javaOptions ++= Seq("-Dconfig.file=src/test/resources/application_test.conf")

lazy val dependencies = new {
  val testcontainersScalaVersion = "0.41.0"

  val mongo = "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.0"
  val json4sExt = "org.json4s" %% "json4s-ext" % "4.0.6"
  val config = "com.typesafe" % "config" % "1.4.2"
  val slf4j = "org.slf4j" % "slf4j-api" % "2.0.9"
  val logback = "ch.qos.logback" % "logback-classic" % "1.4.11"
  val snappy = "org.xerial.snappy" % "snappy-java" % "1.1.9.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15" % Test
  val json4sJackson = "org.json4s" %% "json4s-jackson" % "4.0.6"
  val jsonExt = "org.json4s" %% "json4s-ext" % "4.0.6"
  val testContainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test"
}
lazy val root = (project in file("."))
  .settings(
    organization := "com.abt",
    name := "mongo-testcontainer",
    libraryDependencies ++= Seq(
      dependencies.json4sExt,
      dependencies.mongo,
      dependencies.snappy,
      dependencies.config,
      dependencies.json4sJackson,
      dependencies.jsonExt,
      dependencies.slf4j,
      dependencies.logback,
      dependencies.testContainers,
      dependencies.scalaTest
    )
  )
