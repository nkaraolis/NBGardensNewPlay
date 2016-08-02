package models

import reactivemongo.api.collections.bson.BSONCollection
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import reactivemongo.bson._

/**
  * Created by Administrator on 28/07/2016.
  */
case class CustomerDB(customerID: Int, fName: String, lName: String, email: String, phone: String, username: String, password: String, addresses: List[CustomerAddressDB], cardDetails: List[CustomerCardDB])

object CustomerDB {
  var userList: List[BSONDocument] = List[BSONDocument]()

  var currentCustomer = new CustomerDB(0, "", "", "", "", "", "", List(), List())

  implicit object CustomerDBReader extends BSONDocumentReader[CustomerDB] {
    def read(doc: BSONDocument): CustomerDB =
      CustomerDB(
        doc.getAs[Int]("customerID").get,
        doc.getAs[String]("fName").get,
        doc.getAs[String]("lName").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("phone").get,
        doc.getAs[String]("username").get,
        doc.getAs[String]("password").get,
        doc.getAs[List[CustomerAddressDB]]("addresses").get,
        doc.getAs[List[CustomerCardDB]]("cardDetails").get
      )
  }

  implicit object CustomerDBWriter extends BSONDocumentWriter[CustomerDB] {
    def write(customer: CustomerDB): BSONDocument = {
      BSONDocument(
        "customerID" -> customer.customerID,
        "fName" -> customer.fName,
        "lName" -> customer.lName,
        "email" -> customer.email,
        "phone" -> customer.phone,
        "username" -> customer.username,
        "password" -> customer.password,
        "addresses" -> customer.addresses,
        "cardDetails" -> customer.cardDetails
      )
    }
  }

  /** Finds customer by username and returns CustomerDB object **/
  def findCustomer(username: String)(implicit ec: ExecutionContext): CustomerDB = {
    val findQuery = BSONDocument(
      "username" -> username
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery).one[CustomerDB]
    foundUser onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        println(readResult.get.username)
        currentCustomer = readResult.get
    }
    Thread.sleep(500)
    currentCustomer
  }

  /** Find customer by username **/
  def findByUsername(username: String): List[BSONDocument] = {
    val findQuery = BSONDocument(
      "username" -> username
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery).cursor[BSONDocument]().collect[List]()
    foundUser.onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        userList = readResult
    }
    Thread.sleep(500)
    userList
  }

  /** Find customer by email **/
  def findByEmail(email: String): List[BSONDocument] = {
    val findQuery = BSONDocument(
      "email" -> email
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery).cursor[BSONDocument]().collect[List]()
    foundUser.onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        userList = readResult
    }
    Thread.sleep(500)
    userList
  }

  /** Finds the next customer ID to add in **/
  def findNextID(): Int = {
    var nextID = 0
    val findAll = BSONDocument(
      (null, null)
    )
    val foundID = MongoConnector.collectionCustomer.find(findAll).cursor[BSONDocument]().collect[List]()
    foundID onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        nextID = readResult.size + 1
    }
    Thread.sleep(500)
    nextID
  }

  /** Updates the customer's field in the database **/
  def updateUserField(username: String, updateField: String, value: String): Unit = {
    val selector = BSONDocument("username" -> username)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        updateField -> value))

    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    Thread.sleep(500)
  }
}
