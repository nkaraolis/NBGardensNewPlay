package models

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


/**
  * Created by Administrator on 08/07/2016.
  */
case class Product (productId: String, name: String, description: String, price: String, imgS: String, imgL: String, qty: String, carId: String)

object Product{

  //DB Connection
  val dbn = "NBGardensProducts"
  val user = "user2"
  val pass = "password"
  val creds = List(Authenticate(dbn,user,pass))
  val servs: List[String] = List("192.168.1.42:27017")
  val config = ConfigFactory.load()
  val driver = new MongoDriver(Some(config))
  val conn = driver.connection(servs, authentications=creds)
  val strat = FailoverStrategy()
  val db = conn.db(dbn, strat)
  val coll = db.collection[BSONCollection]("product")

  var products: Set[Product]  = Set.empty

  val key = BSONDocument(
    ("_Id" -> BSONDocument("$exists" -> "1"))
  )

  //  val key = new BasicDBObject()
  //  key.put("_id", false)
  //  key.put("productId", true)
  //  key.put("name", true)
  //  key.put("description", true)
  //  key.put("price", true)
  //  key.put("imgS", true)
  //  key.put("imgL", true)
  //  key.put("need", true)
  //  key.put("carId", true)

  //val productsInDB = coll.find(null, key).cursor[BSONDocument]().collect[List]()
  val productsInDB = coll.find(key).cursor[BSONDocument]().collect[List]()

  productsInDB.onComplete {
    case Failure(e) => throw e
    case Success(readResult) =>
      for (prod <- readResult) {
        //for (p <- prod){
        products += productReader.read(prod)
        println("Product ID" + prod.get("productId").get)
        //}
      }
      println("Size: " + products.size)
      println(products.tail.head.name)
  }

  implicit object productReader extends BSONDocumentReader[Product]{
    def read(doc: BSONDocument):Product = Product(
      doc.getAs[String]("productId").get,
      doc.getAs[String]("name").get,
      doc.getAs[String]("description").get,
      doc.getAs[String]("price").get,
      doc.getAs[String]("imgS").get,
      doc.getAs[String]("imgL").get,
      doc.getAs[String]("qty").get,
      doc.getAs[String]("carId").get)
  }

  implicit object ProductWriter extends BSONDocumentWriter[Product] {
    def write(product: Product) : BSONDocument = BSONDocument(
      "productId" -> product.productId,
      "name" -> product.name,
      "description" -> product.description,
      "price" -> product.price,
      "imgS" -> product.imgS,
      "imgL" -> product.imgL,
      "qty" -> product.qty,
      "carId" -> product.carId
    )
  }
  //  for (p <- productsInDB){
  //    getDetail(p.head)
  //    def getDetail(coll: BSONCollection): List ={
  //
  //    }

  //    Product(p.head.productId, p.name, p.description, p.price, p.imgS, p.imgL, p.need, p.carId)
  //products += p
  //println(products)



  products += (
    Product("0001","Paperclips Large","Large Plain Pack of 1000", "100", "images/page3_img1.jpg", "images/big1.jpg", "", "Lawnmower")
    //    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "100", "images/page3_img2.jpg", "images/big2.jpg", "", "Lawnmower"),
    //    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "100", "images/page3_img3.jpg", "images/big3.jpg", "", "Lawnmower"),
    //    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "100", "images/page3_img4.jpg", "images/big4.jpg", "", "Barbecues"),
    //    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img5.jpg", "images/big5.jpg", "", "Barbecues"),
    //    Product("0006","AA", "No Tear Extra Large Pack of 1000", "100", "images/page3_img7.jpg", "images/big7.jpg", "", "Furniture"),
    //    Product("0008","CC", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
    //    Product("0009","DD", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
    //    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "10", "Furniture")
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
        tl += Product(product.productId, product.name, product.description,product.price, product.imgS, product.imgL, product.qty, product.carId)
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
        if (products.head.name.toLowerCase().contains(name.toLowerCase()))
          filter(products.tail, results + products.head)
        else
          filter(products.tail, results)
      }
    }
    filter(products, Set.empty[Product]).toList
  }


  //def findByNameS(name: String) = products.toList.find(_.name contains(name.toUpperCase()))
  def findByNameS(name: String) = products.toList.find(_.name.toLowerCase().contains(name.toLowerCase()))
}








//package models
//
///**
//  * Created by Administrator on 08/07/2016.
//  */
//case class Product(productId: String, name: String, description: String, price: String, imgS: String, imgL: String, need: String, carId: String)
//
//object Product {
//  var products = Set(
//
//    Product("0001","Paperclips Large","Large Plain Pack of 1000", "100", "images/page3_img1.jpg", "images/big1.jpg", "", "Lawnmower"),
//    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "100", "images/page3_img2.jpg", "images/big2.jpg", "", "Lawnmower"),
//    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "100", "images/page3_img3.jpg", "images/big3.jpg", "", "Lawnmower"),
//    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "100", "images/page3_img4.jpg", "images/big4.jpg", "", "Barbecues"),
//    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img5.jpg", "images/big5.jpg", "", "Barbecues"),
//    Product("0006","AA", "No Tear Extra Large Pack of 1000", "100", "images/page3_img7.jpg", "images/big7.jpg", "", "Furniture"),
//    Product("0008","CC", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
//    Product("0009","DD", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture"),
//    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "10", "Furniture")
//
//  )
//
//  def getPrice(qty:String, price:String): Double ={
//    val tPrice = (qty.toDouble) * (price.toDouble)
//    tPrice
//  }
//
//  def findAll = products.toList.sortBy(_.name)
//
//  def findByName(user: String) = products.find(_.name == user)
//
////  def findByCart(cart: String) = products.find(_.carId == cart).toList.sortBy(_.name)
//
//  def findByCart(cart: String): List[Product] = {
//    var tl: Set[Product]  = Set.empty
//    for (product <- products){
//      if (product.carId == cart){
//        tl += Product(product.productId, product.name, product.description,product.price, product.imgS, product.imgL, product.need, product.carId)
//      }
//    }
//    tl.toList
//  }
//
//  def findProductByName(name: String) = products.find(_.name == name)
//
//
//  def add(Id: String, Name: String, description: String, price: String, imgS: String, imgL: String, need: String, carId: String): Unit ={
//    products += Product(Id,Name,description,price,imgS,imgL, need, carId)
//  }
//
//  def removeFromProduct(product: Product): Set[Product] ={
//    def checkOldList(productsOld: Array[Product], product: Product): Array[Product] = {
//      if (productsOld.isEmpty) {
//        productsOld
//      }
//      else if (product.productId == productsOld.head.productId) {
//        checkOldList(productsOld.tail, product)
//      }
//      else {
//        checkOldList(productsOld.tail, product) :+ productsOld.head
//      }
//    }
//    products = checkOldList(products.toArray, product).toSet
//    products
//  }
//
//  def setQTY (name: String, need: String): Unit ={
//    def findByName(name: String) = {
//      val tp = products.toList.find(_.name == name).get
//      removeFromProduct(tp)
//      add(tp.productId,tp.name,tp.description,tp.price,tp.imgS,tp.imgL,need,tp.carId)
//    }
//    findByName(name)
//  }
//
//  def findByNameOB(name: String) ={
//    def filter(products: Set[Product], results: Set[Product]) : Set[Product] ={
//      if (products.isEmpty)
//        results
//      else {
//        if (products.head.name.toLowerCase().contains(name.toLowerCase()))
//          filter(products.tail, results + products.head)
//        else
//          filter(products.tail, results)
//      }
//    }
//    filter(products, Set.empty[Product]).toList
//  }
//
//  //def findByNameS(name: String) = products.toList.find(_.name contains(name.toUpperCase()))
//  def findByNameS(name: String) = products.toList.find(_.name.toLowerCase().contains(name.toLowerCase()))
//}
