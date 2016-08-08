package models
import java.io.{BufferedWriter, File, FileWriter}

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
/**
  * Created by Administrator on 21/07/2016.
  */
case class CardDetails (cardName:String, nameOnCard:String, cardNo:String, startDate: String, expiryDate: String, securityCode:String, issueNo:String)

//case class CardDetails (method:String)

object CardDetails {

  var cards : Set[CardDetails] = Set.empty

  val fileName = "app\\data\\cardDetails.csv"
  val file = new File(fileName)


  def add(card: CardDetails): Unit = {
    cards += card
    //saveToCSV(cards)
  }



  def saveToCSV(cards: Set[CardDetails]): Unit ={
    val writer = new BufferedWriter(new FileWriter(file))
    for (detail <- cards){
      val text = detail.nameOnCard + ", " + detail.cardNo + ", " + detail.startDate + ", " + detail.expiryDate + ", " + detail.securityCode + ", " + detail.issueNo + "\n"
      writer.write(text)
    }
    writer.close()
  }


  implicit object CardDetailsReader extends BSONDocumentReader[CardDetails]{
    def read(doc: BSONDocument):CardDetails = CardDetails(
      doc.getAs[String]("cardName").get,
      doc.getAs[String]("nameOnCard").get,
      doc.getAs[String]("cardNo").get,
      doc.getAs[String]("startDate").get,
      doc.getAs[String]("expiryDate").get,
      doc.getAs[String]("securityCode").get,
      doc.getAs[String]("issueNo").get
    )
  }

  implicit object CardDetailsWriter extends BSONDocumentWriter[CardDetails] {
    def write(card: CardDetails) : BSONDocument = BSONDocument(
      "cardName" -> card.cardName,
      "nameOnCard" -> card.nameOnCard,
      "cardNo" -> card.cardNo,
      "startDate" -> card.startDate,
      "expiryDate" -> card.expiryDate,
      "securityCode" -> card.securityCode,
      "issueNo" -> card.issueNo
    )
  }



}
