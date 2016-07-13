package models


/**
  * Created by Administrator on 12/07/2016.
  */
object DataDump {


  def getCart: Array[Product] = {

    Array{
      Product("0001","Paperclips Large","Large Plain Pack of 1000", "£100", "images/page3_img1.jpg", "images/big1.jpg")
    }

  }



//
//  def getCart2: Array[Product] = {
//
//    Array {
//      Product("0007", "BB", "No Tear Extra Large Pack of 1000", "£100", "images/page3_img7.jpg", "images/big7.jpg"),
//      Product("0008", "CC", "Zebra Length 28mm Assorted 150 Pack", "£100", "images/page3_img8.jpg", "images/big8.jpg"),
//      Product("0009", "dd", "Zebra Length 28mm Assorted 150 Pack", "£100", "images/page3_img8.jpg", "images/big8.jpg")
//    }
//  }




}
