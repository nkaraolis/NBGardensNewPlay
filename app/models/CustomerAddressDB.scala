package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerAddressDB(addressID: Int, fullName: String, addressType: String, line1: String, line2: String, townCity: String, county: String, postcode: String)

object CustomerAddressDB {
  
  /** Creates the reader and writer for the CustomerAddressDB case class */
  implicit val addressBSONHandler = Macros.handler[CustomerAddressDB]

  /** Find address by address ID **/
  def findAddress(customer: CustomerDB, addressID : Int): CustomerAddressDB = customer.addresses.find(_.addressID == addressID).get

  /** Update customer address **/
  def updateAddress(username: String, newAddress: CustomerAddressDB, updater: String): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be addresses
    val modifier = BSONDocument(
      updater -> BSONDocument(
        "addresses" -> newAddress))
    // Runs the update query
    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    runUpdate onComplete {
      Success =>
        println("Address updated")
    }
    Thread.sleep(500)
  }

  /** Delete an address **/
  def removeAddress(username: String, address: CustomerAddressDB): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be addresses
    val modifier = BSONDocument(
      "$pull" -> BSONDocument(
        "addresses" -> address))
    // Runs the update query
    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    runUpdate onComplete {
      Success =>
        println("Address deleted")
    }
  }


}