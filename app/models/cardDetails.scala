package models
import java.io.{BufferedWriter, File, FileWriter}
/**
  * Created by Administrator on 21/07/2016.
  */
case class cardDetails (method:String, name:String, cardNu:String, exp: String, securityCode: String, issueNu:String, start:String)

object cardDetails {

  var cards : Set[cardDetails] = Set.empty

  val fileName = "app\\data\\cardDetails.csv"
  val file = new File(fileName)

  def add(card: cardDetails): Unit = {
    cards += card
    saveToCSV(cards)
  }

  def saveToCSV(cards: Set[cardDetails]): Unit ={
    val writer = new BufferedWriter(new FileWriter(file))
    for (detail <- cards){
      val text = detail.method + ", " + detail.name + ", " + detail.cardNu + ", " + detail.exp + ", " + detail.securityCode + ", " + detail.issueNu + ", " + detail.start + "\n"
      writer.write(text)
    }
    writer.close()
  }
}
