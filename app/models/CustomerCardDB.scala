package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 26/07/2016.
  */
//case class CustomerCardDB(cardType: String, cardNumber: String, expiry: String, name: String)
case class CustomerCardDB(cardID: Int, cardType: String, cardNumber: String, expiry: String, NoC: String)

object CustomerCardDB {

  /** Creates the reader and writer for the CustomerCardDB case class */
  implicit val cardBSONHandler = Macros.handler[CustomerCardDB]


  var cards : Set[CustomerCardDB] = Set.empty


  def add(card: CustomerCardDB): Unit = {
    cards += card
  }


//  def loadCards(username: String): Set [CustomerCardDB] = {
//    println("customer username: " + username)//this now prints out the correct username
//    val cus = CustomerDB.findCustomer(username)
//
//    println("Is there anything in cus.CardDetails?: " + cus.cardDetails) //this is still set to the preset
//    cards = cus.cardDetails.toSet
//    println("These are the cards: " + cards) //the cards set returns empty
//    cards
//  }


  def findCard(number: String) = cards.find(_.cardNumber == number)

  def selectCard(id: Int) = cards.find(_.cardID == id)



  /** Find card details by card ID **/
  def findCardDetails(customer: CustomerDB, cardID : Int): CustomerCardDB = customer.cardDetails.find(_.cardID == cardID).get

  /** Update customer card details **/
  def updatePayment(username: String, newCard: CustomerCardDB, updater: String): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be card details
    val modifier = BSONDocument(
      updater -> BSONDocument(
        "cardDetails" -> newCard))
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