package models

import com.typesafe.config.ConfigFactory
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.core.nodeset.Authenticate

/**
  * Created by Administrator on 29/07/2016.
  */
class MongoDBConnect {

  //DB Connection
  val dbn = "NBGardensOrders"
  val user = "user2"
  val pass = "password"
  val creds = List(Authenticate(dbn,user,pass))
  val servs: List[String] = List("192.168.1.42:27017")
  val config = ConfigFactory.load()
  val driver = new MongoDriver(Some(config))
  val conn = driver.connection(servs, authentications=creds)
  val strat = FailoverStrategy()
  val db = conn.db(dbn, strat)
  val coll = db.collection[BSONCollection]("product")






}
