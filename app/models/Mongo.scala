//package models
//
//import com.typesafe.config.ConfigFactory
//import play.api.db
//import reactivemongo.api.{FailoverStrategy, MongoDriver}
//import reactivemongo.api.collections.bson.BSONCollection
//import reactivemongo.core.commands.Authenticate
//
//import com.typesafe.config.ConfigFactory
//import reactivemongo.api.{FailoverStrategy, MongoDriver}
//import reactivemongo.api.collections.bson.BSONCollection
//import reactivemongo.api.commands.WriteResult
//import reactivemongo.bson.BSONDocument
//import reactivemongo.core.nodeset.Authenticate
//
//import scala.concurrent.ExecutionContext._
//import scala.concurrent.Future
//import scala.util.{Failure, Success}
///**
//  * Created by Administrator on 25/07/2016.
//  */
//class Mongo {
//
//object MongoConnection{
//  val dbn = "NBGardensProducts"
//  val user = "Zeph"
//  val pass = "password"
//  val creds = List(Authenticate(
//    dbn, user, pass))
//
//  val conn = driver.connection(servs, authentications = creds)
//
//  val coll = db.collection[BSONCollection]("collN")
//  val strat = FailoverStrategy()
//  // /DATABaseName
//  def servs : List [String] = List("192.168.1.42:27017")
//  def config = ConfigFactory.load()
//  def driver = new MongoDriver(Some(config))
//  def db = conn.db(dbn, strat)
//
//  //new entry
//  val doc = BSONDocument("fname" -> "Zephaniah", "lNAme"->20)
//  val futIns : Future[WriteResult] = coll.insert[BSONDocument](doc)
//
//  futIns.onComplete {
//    case Failure(e) => throw e
//    case Success(writeResult) => println(s"success: $writeResult")
//  }
//
//  val end : Future[Unit] = futIns.map {
//    case writeResult if (writeResult.code contains 11000) => println("Warning 11000:Duplicate")
//    case _ => ()
//  }
//}
//
//}
