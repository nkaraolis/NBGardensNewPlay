package models

import java.io.{BufferedWriter, File, FileWriter}

/**
  * Created by Administrator on 25/07/2016.
  */
case class Orders (product: String)

object writeOrders {

  var orders : Set[Orders] = Set.empty

  val fileName = "app\\data\\OrderDetails.csv"
  val file = new File(fileName)

  def add(order: String): Unit = {
    orders += Orders(order)
    saveToCSV(orders)
  }

  def saveToCSV(orders: Set[Orders]): Unit ={
    val writer = new BufferedWriter(new FileWriter(file))
    for (detail <- orders){
      val text = detail.product + "\n"
      writer.write(text)
    }
    writer.close()
  }

//  def main(args: Array[String]) {
//    for (d <- orders)
//    {
//      println(d)
//    }
//  }

  def findOrders = orders.toList

}