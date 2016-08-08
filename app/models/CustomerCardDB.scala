package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerCardDB(cardType: String, cardNumber: String, expiry: String, name: String)

object CustomerCardDB {

//  implicit object CustomerCardReader extends BSONDocumentReader[CustomerCardDB]{
//    def read (doc : BSONDocument) :
//    CustomerCardDB = CustomerCardDB (
//      doc.getAs[String]("cardType").get,
//      doc.getAs[String]("cardNumber").get,
//      doc.getAs[String]("expiry").get,
//      doc.getAs[String]("name").get
//    )
//  }
//
//  implicit object CustomerCardWriter extends BSONDocumentWriter[CustomerCardDB]{
//    def write (card : CustomerCardDB) :
//    BSONDocument = BSONDocument(
//      "cardType" -> card.cardType,
//      "cardNumber" -> card.cardNumber,
//      "expiry" -> card.expiry,
//      "name" -> card.name
//    )
//  }


  var cards : Set[CustomerCardDB] = Set.empty


  def add(card: CustomerCardDB): Unit = {
    cards += card
  }


  /** Creates the reader and writer for the CustomerCardDB case class */
  implicit val cardBSONHandler = Macros.handler[CustomerCardDB]

  /** Update customer card details **/
  def updatePayment(username: String, value: CustomerCardDB, updater: String): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be addresses
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



  def loadCards(username: String): Set [CustomerCardDB] = {
    val cus = CustomerDB.findCustomer(username)
    cards = cus.cardDetails.toSet
    println("These are the cards: " + cards)
    cards

  }

 def findCard(number: String) = cards.find(_.cardNumber == number)



}