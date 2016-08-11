package models

import javax.inject.Singleton

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
@Singleton
object MongoConnector {

  /** Database names to connect to **/
  val dbCustomers = "NBGardensCustomers"
  val dbProducts = "NBGardensProducts"
  val dbOrders = "NBGardensOrders"
  val dbContacts = "NBGardensContacts"
  val dbCatalogues = "NBGardensCatalogues"

  /** Setup username and password required for customers DB connection **/
  val userCustomer = "customerAdmin"
  val passCustomer = "1234"
  val credsCustomer = List(Authenticate(dbCustomers, userCustomer, passCustomer))

  /** Setup username and password for contact DB connection**/
  val userContact = "contactAdmin"
  val passContact = "1234"
  val credsContact = List(Authenticate(dbContacts, userContact, passContact))

  /** Setup username and password for catalogue DB connection**/
  val userCatalogue = "catalogueAdmin"
  val passCatalogue = "1234"
  val credsCatalogue = List(Authenticate(dbCatalogues, userCatalogue, passCatalogue))

  /** Setup username and password required for orders DB connection **/
  val userOrder = "ordersAdmin"
  val passOrder = "1234"
  val credsOrder = List(Authenticate(dbOrders, userOrder, passOrder))

  /** Setup username and password required for products DB connection **/
  val userProduct = "productAdmin"
  val passProduct = "1234"
  val credsProduct = List(Authenticate(dbProducts, userProduct, passProduct))

  def servers: List[String] = List("192.168.1.42:27017")
  def config = ConfigFactory.load()
  def driver = new MongoDriver(Some(config))
  val errorStrategy = FailoverStrategy()

  val connectCustomer = driver.connection(servers, authentications = credsCustomer)
  val connectContact = driver.connection(servers, authentications = credsContact)
  val connectCatalogue = driver.connection(servers, authentications = credsCatalogue)
  val connectOrder = driver.connection(servers, authentications = credsOrder)
  val connectProduct = driver.connection(servers, authentications = credsProduct)

  /** Database connections  **/
  def dbCustomersCon = connectCustomer.db(dbCustomers, errorStrategy)
  def dbContactCon = connectContact.db(dbContacts, errorStrategy)
  def dbCatalogueCon = connectCatalogue.db(dbCatalogues, errorStrategy)
  def dbOrdersCon = connectOrder.db(dbOrders, errorStrategy)
  def dbProductsCon = connectProduct.db(dbProducts, errorStrategy)

  /** Database collections  **/
  val collectionCustomer = dbCustomersCon.collection[BSONCollection]("customer")
  var collectionContact = dbContactCon.collection[BSONCollection]("contact")
  var collectionCatalogue = dbCatalogueCon.collection[BSONCollection]("catalogue")
  val collectionOrder = dbOrdersCon.collection[BSONCollection]("order")
  val collectionProduct = dbProductsCon.collection[BSONCollection]("product")

  def updateAddress(): Unit = {

  }

}