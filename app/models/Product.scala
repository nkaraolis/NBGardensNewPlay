package models

/**
  * Created by Administrator on 08/07/2016.
  */
case class Product(productId: String, Name: String, description: String)

object Product {
  var products = Set(
    Product("0001","Paperclips Large","Large Plain Pack of 1000"),
    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack"),
    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000"),
    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000"),
    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack")
  )

  def findAll = products.toList.sortBy(_.Name)

  def add(Id: String, Name: String, description: String): Unit ={
    products += Product(Id,Name,description)
  }
}
