package models

/**
  * Created by Administrator on 08/07/2016.
  */
case class Product(productId: String, Name: String, description: String, price: String, imgS: String, imgL: String, need: String)

object Product {
  var products = Set(
<<<<<<< HEAD
    Product("0001","Paperclips Large","Large Plain Pack of 1000", "£100", "images/page3_img1.jpg", "images/big1.jpg", ""),
    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "£100", "images/page3_img2.jpg", "images/big2.jpg", ""),
    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "£100", "images/page3_img3.jpg", "images/big3.jpg", ""),
    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "£100", "images/page3_img4.jpg", "images/big4.jpg", ""),
    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "£100", "images/page3_img5.jpg", "images/big5.jpg", ""),
    Product("0006","AA", "Giant Plain Pack of 10000", "£100", "images/page3_img6.jpg", "images/big6.jpg", ""),
    Product("0007","BB", "No Tear Extra Large Pack of 1000", "£100", "images/page3_img7.jpg", "images/big7.jpg", ""),
    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "£100", "images/page3_img8.jpg", "images/big8.jpg", "10")
=======
    Product("0001","Paperclips Large","Large Plain Pack of 1000", "£100.00", "images/page3_img1.jpg", "images/big1.jpg", ""),
    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "£100.00", "images/page3_img2.jpg", "images/big2.jpg", ""),
    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "£100.00", "images/page3_img3.jpg", "images/big3.jpg", ""),
    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "£100.00", "images/page3_img4.jpg", "images/big4.jpg", ""),
    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "£100.00", "images/page3_img5.jpg", "images/big5.jpg", ""),
    Product("0006","AA", "Giant Plain Pack of 10000", "£100.00", "images/page3_img6.jpg", "images/big6.jpg", ""),
    Product("0007","BB", "No Tear Extra Large Pack of 1000", "£100.00", "images/page3_img7.jpg", "images/big7.jpg", ""),
    Product("0008","CC", "Zebra Length 28mm Assorted 150 Pack", "£100.00", "images/page3_img8.jpg", "images/big8.jpg", ""),
    Product("0009","DD", "Zebra Length 28mm Assorted 150 Pack", "£100.00", "images/page3_img8.jpg", "images/big8.jpg", ""),
    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "£100.00", "images/page3_img8.jpg", "images/big8.jpg", "10")
>>>>>>> bb3d84804ca7faf8a6e12d610ffb6af675aceba8
  )

  def findAll = products.toList.sortBy(_.Name)

  def findProductByName(name: String) = products.find(_.Name == name)

  def findByName(name: String) = products.toList.find(_.Name == name)

  def add(Id: String, Name: String, description: String, price: String, imgS: String, imgL: String, need: String): Unit ={
    products += Product(Id,Name,description,price,imgS,imgL, need)
  }

  def removeFromProduct(product: Product): Set[Product] ={
    def checkOldList(productsOld: Array[Product], product: Product): Array[Product] = {
      if (productsOld.isEmpty) {
        productsOld
      }
      else if (product.productId == productsOld.head.productId) {
        checkOldList(productsOld.tail, product)
      }
      else {
        checkOldList(productsOld.tail, product) :+ productsOld.head
      }
    }
    products = checkOldList(products.toArray, product).toSet
    products
  }

  def setQTY (name: String, need: String): Unit ={
    def findByName(name: String) = {
      val tp = products.toList.find(_.Name == name).get
      removeFromProduct(tp)
      add(tp.productId,tp.Name,tp.description,tp.price,tp.imgS,tp.imgL,need)
    }
    findByName(name)
  }
}
