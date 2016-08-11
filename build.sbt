name := """NBGardensNewPlay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.reactivemongo" % "reactivemongo_2.11" % "0.11.12",
  "com.thenewmotion.akka" %% "akka-rabbitmq" % "2.3",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2"



)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"




