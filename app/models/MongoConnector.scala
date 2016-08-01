package models

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by Administrator on 25/07/2016.
  */
object MongoConnector {

  /** Specify database names to connect to **/
  val dbCustomers = "NBGardensCustomers"
  val dbProducts = "NBGardensProducts"
  val dbOrders = "NBGardensOrders"

  /** Setup username and password required for DB connection **/
  val userCustomer = "customerAdmin"
  val passCustomer = "1234"
  val credsCustomer = List(Authenticate(dbCustomers, userCustomer, passCustomer))

  def servers: List[String] = List("192.168.1.42:27017")

  def config = ConfigFactory.load()

  def driver = new MongoDriver(Some(config))

  val connectCustomer = driver.connection(servers, authentications = credsCustomer)

  val errorStrategy = FailoverStrategy()

  def dbCustomersCon = connectCustomer.db(dbCustomers, errorStrategy)

  val collectionCustomer = dbCustomersCon.collection[BSONCollection]("customer")

  def updateAddress(): Unit = {

  }

}