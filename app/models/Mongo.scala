package models

import com.typesafe.config.ConfigFactory
import play.api.db
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.core.commands.Authenticate
import sun.text.normalizer.ICUBinary.Authenticate

/**
  * Created by Administrator on 25/07/2016.
  */
class Mongo {

object MongoConnection{
  val dbn = "NBGardensProducts"
  val user = "Zeph"
  val pass = "password"
  val creds = List(Authenticate(
    dbn, user, pass))

  val conn = driver.connection(servs, authentications = creds)

  val coll = db.collection[BSONCollection]("collN")
  val strat = FailoverStrategy()
  // /DATABaseName
  def servs : List [String] = List("192.168.1.42:27017")
  def config = ConfigFactory.load()
  def driver = new MongoDriver(Some(config))
  def db = conn.db(dbn, strat)

}

}
