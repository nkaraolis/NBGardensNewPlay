package models

/**
  * Created by Administrator on 08/07/2016.
  */
case class Product(productId: String, name: String, description: String, price: String, imgS: String, imgL: String, need: String, carId: String)

object Product {
  var products = Set(

    Product("0001","Paperclips Large","Large Plain Pack of 1000", "100", "images/page3_img1.jpg", "images/big1.jpg", "", "Lawnmower"),
    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "100", "images/page3_img2.jpg", "images/big2.jpg", "", "Lawnmower"),
    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "100", "images/page3_img3.jpg", "images/big3.jpg", "", "Lawnmower"),
    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "100", "images/page3_img4.jpg", "images/big4.jpg", "", "Barbecues"),
    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img5.jpg", "images/big5.jpg", "", "Barbecues"),
    Product("0006","AA", "No Tear Extra Large Pack of 1000", "100", "images/page3_img7.jpg", "images/big7.jpg", "", "Furniture"),
    Product("0008","CC", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
    Product("0009","DD", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "10", "Furniture")

  )

  def getPrice(qty:String, price:String): Double ={
    val tPrice = (qty.toDouble) * (price.toDouble)
    tPrice
  }

  def findAll = products.toList.sortBy(_.name)

  def findByName(user: String) = products.find(_.name == user)

//  def findByCart(cart: String) = products.find(_.carId == cart).toList.sortBy(_.name)

  def findByCart(cart: String): List[Product] = {
    var tl: Set[Product]  = Set.empty
    for (product <- products){
      if (product.carId == cart){
        tl += Product(product.productId, product.name, product.description,product.price, product.imgS, product.imgL, product.need, product.carId)
      }
    }
    tl.toList
  }

  def findProductByName(name: String) = products.find(_.name == name)


  def add(Id: String, Name: String, description: String, price: String, imgS: String, imgL: String, need: String, carId: String): Unit ={
    products += Product(Id,Name,description,price,imgS,imgL, need, carId)
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
      val tp = products.toList.find(_.name == name).get
      removeFromProduct(tp)
      add(tp.productId,tp.name,tp.description,tp.price,tp.imgS,tp.imgL,need,tp.carId)
    }
    findByName(name)
  }

  def findByNameOB(name: String) ={
    def filter(products: Set[Product], results: Set[Product]) : Set[Product] ={
      if (products.isEmpty)
        results
      else {
        if (products.head.name contains(name))
          filter(products.tail, results + products.head)
        else
          filter(products.tail, results)
      }
    }
    filter(products, Set.empty[Product]).toList
  }

  def findByNameS(name: String) = products.toList.find(_.name contains(name.toUpperCase()))

}
