package models

import com.typesafe.config.ConfigFactory
import models.OrderDB.OrderDBReader
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import reactivemongo.core.nodeset.Authenticate
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by Administrator on 02/08/2016.
  */
case class CartItem (proId: Int, proName: String, quantity: Int, unitPrice:Double)


object CartItem {

  def findByName(name: String) = Cart.productsInCart.find(_.proName == name)
//
//
//  //DB Connection
//  val dbn = "NBGardensProducts"
//  val user = "user2"
//  val pass = "password"
//  val creds = List(Authenticate(dbn,user,pass))
//  val servs: List[String] = List("192.168.1.42:27017")
//  val config = ConfigFactory.load()
//  val driver = new MongoDriver(Some(config))
//  val conn = driver.connection(servs, authentications=creds)
//  val strat = FailoverStrategy()
//  val db = conn.db(dbn, strat)
//  //var coll = db.collection[BSONCollection]("product")
//
//  var products: Set[CartItem]  = Set.empty
//
//  //empty condition so that all items in this database are returned and not one in particular
//  val condition = BSONDocument(
//    (null, null)
//  )
//
//
//  val key = BSONDocument(
//    ("_id" -> false)
//  )
//
//
//  //val productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()
//
//
////  productsInDB.onComplete {
////    case Failure(e) => throw e
////    case Success(readResult) =>
////      for (prod <- readResult) {
////        products += CartItemReader.read(prod)
////      }
////  }
//
//
  var cartitems: Set[CartItem]  = Set.empty

  def add(id: Int, name: String, quantity: Int, price: Double): Unit ={
    cartitems += CartItem(id,name,quantity,price)
  }


//
//
//  def findByName(user: String) = cartitems.find(_.proName == user)
//
//
//
//def removeFromProduct(product: CartItem): Set[CartItem] ={
//  def checkOldList(productsOld: Array[CartItem], product: CartItem): Array[CartItem] = {
//    if (productsOld.isEmpty) {
//      productsOld
//    }
//    else if (product.proId == productsOld.head.proId) {
//      checkOldList(productsOld.tail, product)
//    }
//    else {
//      checkOldList(productsOld.tail, product) :+ productsOld.head
//    }
//  }
//  Cart.productsInCart = checkOldList(Cart.productsInCart, product)
//
//  Cart.productsInCart
//}




  implicit object CartItemReader extends BSONDocumentReader[CartItem]{
    def read(doc: BSONDocument):CartItem = CartItem(
      doc.getAs[Int]("proId").get,
      doc.getAs[String]("proName").get,
      doc.getAs[Int]("quantity").get,
      doc.getAs[Double]("unitPrice").get)
  }

  implicit object CartItemWriter extends BSONDocumentWriter[CartItem] {
    def write(product: CartItem) : BSONDocument = BSONDocument(
      "proId" -> product.proId,
      "proName" -> product.proName,
      "quantity" -> product.quantity,
      "unitPrice" -> product.unitPrice
    )
  }
//
//
//  def saveProductsForAnOrder(products: String): Unit = {
//    /** String to Product */
//    var productsForAnOrder: Set[CartItem] = Set.empty
//
//    val newOrder: Array[String] = products.split("CartItem")
//    for (p <- newOrder) {
//      val pro: Array[String] = p.split(",")
//      if (pro.length == 4) {
//        productsForAnOrder += CartItem(pro(0).toInt, pro(1), pro(2).toInt, pro(3).toDouble) //Product(pro.apply(0), pro.apply(1), pro.apply(2), pro.apply(3), pro.apply(4), pro.apply(5), pro.apply(6), pro.apply(7), pro.apply(8), pro.apply(9))
//        //productsForAnOrder += CartItem("","","","")
//      }
//    }
//
//    val coll = db.collection[BSONCollection]("productsInOrders")
//
//    /** Write to DB */
//    for (p <- productsForAnOrder) {
//      val key = BSONDocument(
//        "proId" -> p.proId,
//        "proName" -> p.proName,
//        "quantity" -> p.quantity,
//        "unitPrice" -> p.unitPrice
//      )
//      val futIns: Future[WriteResult] = coll.insert[BSONDocument](key)
//      futIns.onComplete {
//        case Failure(e) => throw e
//        case Success(writeResult) =>
//          println("success writeResult")
//      }
//    }
//  }
//
//

}

