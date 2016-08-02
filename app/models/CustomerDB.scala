package models

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

import scala.util.{Failure, Success}

/**
  * Created by Administrator on 28/07/2016.
  */
case class CustomerDB(id: BSONObjectID, customerID: Int, fName: String, lName: String, email: String, telephone: String, username: String, password: String, addresses: List[CustomerAddressDB], cardDetails: List[CustomerCardDB])

object CustomerDB {
  var userList: List[BSONDocument] = List(BSONDocument())

  implicit object CustomerDBReader extends BSONDocumentReader[CustomerDB] {
    def read(doc: BSONDocument): CustomerDB =
      CustomerDB(doc.getAs[BSONObjectID]("_id").get,
        doc.getAs[Int]("customerID").get,
        doc.getAs[String]("fName").get,
        doc.getAs[String]("lName").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("telephone").get,
        doc.getAs[String]("username").get,
        doc.getAs[String]("password").get,
        doc.getAs[List[CustomerAddressDB]]("addresses").get,
        doc.getAs[List[CustomerCardDB]]("cardDetails").get
      )
  }

  implicit object CustomerWriter extends BSONDocumentWriter[CustomerDB] {
    def write(customer: CustomerDB): BSONDocument = {
      BSONDocument(
        "customerID" -> customer.customerID,
        "fName" -> customer.fName,
        "lName" -> customer.lName,
        "email" -> customer.email,
        "telephone" -> customer.telephone,
        "username" -> customer.username,
        "password" -> customer.password,
        "addresses" -> customer.addresses,
        "cardDetails" -> customer.cardDetails
      )
    }
  }

  /** Find by username **/
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
    userList
  }

  /** Match the username and password for login **/
  def checkUserCredentials(username: String, password: String): Boolean = {
    var status: Boolean = true
    val findQuery = BSONDocument(
      "username" -> username,
      "password" -> password
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery).cursor[BSONDocument]().collect[List]()
    foundUser onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        if (readResult.nonEmpty) {
          status = true
          println("Status = " + status)
          println("Current user: " + readResult.head.getAs[String]("username").get)
        } else {
          status = false
        }
    }
    Thread.sleep(500)
    println("End of the method status: " + status)
    status
  }

  /** Send recovery email **/
  def findByEmail(collection: BSONCollection, query: BSONDocument): Unit = {
    val foundUserEmail = collection.find(query).cursor[BSONDocument]().collect[List]()
    foundUserEmail.onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        for (user <- readResult) {
          println("Recovery email sent")
        }
    }
  }


}
