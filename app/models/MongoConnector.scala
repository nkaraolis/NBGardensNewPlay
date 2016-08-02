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

  /** Database names to connect to **/
  val dbCustomers = "NBGardensCustomers"
  val dbProducts = "NBGardensProducts"
  val dbOrders = "NBGardensOrders"

  /** Setup username and password required for customers DB connection **/
  val userCustomer = "customerAdmin"
  val passCustomer = "1234"
  val credsCustomer = List(Authenticate(dbCustomers, userCustomer, passCustomer))

  /** Setup username and password required for orders DB connection **/
  val userOrder = "ordersAdmin"
  val passOrder = "1234"
  val credsOrder = List(Authenticate(dbOrders, userOrder, passOrder))

  def servers: List[String] = List("192.168.1.42:27017")

  def config = ConfigFactory.load()

  def driver = new MongoDriver(Some(config))

  val errorStrategy = FailoverStrategy()

  /** Customer database collection  **/
  val connectCustomer = driver.connection(servers, authentications = credsCustomer)
  def dbCustomersCon = connectCustomer.db(dbCustomers, errorStrategy)
  val collectionCustomer = dbCustomersCon.collection[BSONCollection]("customer")

  /** Orders database collection **/
  def dbOrdersCon = connectOrder.db(dbOrders, errorStrategy)
  val connectOrder = driver.connection(servers, authentications = credsOrder)
  val collectionOrder = dbOrdersCon.collection[BSONCollection]("order")
}