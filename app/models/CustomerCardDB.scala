package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerCardDB(cardType: String, cardNumber: String, expiry: String, NoC: String)


object CustomerCardDB {
  implicit object CustomerCardReader extends BSONDocumentReader[CustomerCardDB]{
    def read (doc : BSONDocument) :
    CustomerCardDB = CustomerCardDB (
      doc.getAs[String]("cardType").get,
      doc.getAs[String]("cardNumber").get,
      doc.getAs[String]("expiry").get,
      doc.getAs[String]("NoC").get
    )
  }

  implicit object CustomerCardWriter extends BSONDocumentWriter[CustomerCardDB]{
    def write (card : CustomerCardDB) :
    BSONDocument = BSONDocument(
      "cardType" -> card.cardType,
      "cardNumber" -> card.cardNumber,
      "expiry" -> card.expiry,
      "NoC" -> card.NoC
    )
  }
}