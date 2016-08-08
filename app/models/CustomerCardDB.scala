package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerCardDB(cardID: Int, cardType: String, cardNumber: String, expiry: String, NoC: String)

object CustomerCardDB {

  /** Creates the reader and writer for the CustomerCardDB case class */
  implicit val cardBSONHandler = Macros.handler[CustomerCardDB]

  /** Find card details by card ID **/
  def findCardDetails(customer: CustomerDB, cardID : Int): CustomerCardDB = customer.cardDetails.find(_.cardID == cardID).get

  /** Update customer card details **/
  def updatePayment(username: String, value: CustomerCardDB, updater: String): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be card details
    val modifier = BSONDocument(
      updater -> BSONDocument(
        "cardDetails" -> value))
    // Runs the update query
    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    runUpdate onComplete {
      Success =>
        println("Payment updated")
    }
    Thread.sleep(500)
  }

  /** Delete card details **/
  def removeCardDetails(username: String, cardDetails: CustomerCardDB): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be addresses
    val modifier = BSONDocument(
      "$pull" -> BSONDocument(
        "cardDetails" -> cardDetails))
    // Runs the update query
    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    runUpdate onComplete {
      Success =>
        println("Card details deleted")
    }
  }
}