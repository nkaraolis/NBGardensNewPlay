package models
import java.io.{BufferedWriter, File, FileWriter}
/**
  * Created by Administrator on 21/07/2016.
  */
case class cardDetails (method:String, Name_on_card:String, Card_No:String, Start_Date: String, Expiry_Date: String, Security_Code:String, Issue_No:String)

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
      val text = detail.method + ", " + detail.Name_on_card + ", " + detail.Card_No + ", " + detail.Start_Date + ", " + detail.Expiry_Date + ", " + detail.Security_Code + ", " + detail.Issue_No + "\n"
      writer.write(text)
    }
    writer.close()
  }
}
