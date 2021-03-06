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

  var testingList : Set[CustomerDB] = Set.empty


  /** Creates the reader and writer for the CustomerDB case class */
  implicit val customerBSONHandler = Macros.handler[CustomerDB]



  /** Finds customer by username and returns CustomerDB object **/
  def findCustomer(username: String)(implicit ec: ExecutionContext): CustomerDB = {
    var currentUser = new CustomerDB(0, "", "", "", "", "", "", List[CustomerAddressDB](), List[CustomerCardDB]())
    val findQuery = BSONDocument(
      "username" -> username
    )
    val key = BSONDocument(
      "_id" -> false
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery, key).one[CustomerDB]

    foundUser.onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        currentUser = readResult.get
    }
    Thread.sleep(1500)
    currentUser
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

    //Gets everything from collection
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
  /** This method allows the user to update any of their details **/
  def updateUserField(username: String, updateField: String, value: String): Unit = {
    val selector = BSONDocument("username" -> username)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        updateField -> value))

    MongoConnector.collectionCustomer.update(selector, modifier)
    Thread.sleep(500)
  }
}