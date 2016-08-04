package models

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import reactivemongo.core.nodeset.Authenticate
import scala.collection.SortedSet
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by Administrator on 08/07/2016.
  */
case class Product (productId: String, name: String, description: String, price: String, mainImage: String, secondaryImages: String, qty: String, category: String, porousAllowed: String, reviews: String)


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
  var coll = db.collection[BSONCollection]("product")

  var products: Set[Product]  = Set.empty

  //empty condition so that all items in this database are returned and not one in particular
  val condition = BSONDocument(
    (null, null)
  )


  val key = BSONDocument(
    ("_id" -> false)
  )


  //val productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()

  val productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()


  productsInDB.onComplete {
    case Failure(e) => throw e
    case Success(readResult) =>
      for (prod <- readResult) {
        products += productReader.read(prod)
      }
  }


  def findById(user: String) = products.find(_.productId == user)


  def getImage(id: String): String ={
    val imageStr = findById(id).get.mainImage
    imageStr
  }


  implicit object productReader extends BSONDocumentReader[Product]{
    def read(doc: BSONDocument):Product = Product(
      doc.getAs[String]("productId").get,
      doc.getAs[String]("name").get,
      doc.getAs[String]("description").get,
      doc.getAs[String]("price").get,
      doc.getAs[String]("mainImage").get,
      doc.getAs[String]("secondaryImages").get,
      doc.getAs[String]("qty").get,
      doc.getAs[String]("category").get,
      doc.getAs[String]("porousAllowed").get,
      doc.getAs[String]("reviews").get)
  }


  //Review(var productId: String, var username: String,var reviewTitle: String, var review: String, var reviewDate: String, var rating: String) {

  //(productId: String, name: String, description: String, price: String, mainImage: String, secondaryImages: String, qty: String, category: String, porousAllowed: String, reviews: Set[Review])


  implicit object ProductWriter extends BSONDocumentWriter[Product] {
    def write(product: Product) : BSONDocument = BSONDocument(
      "productId" -> product.productId,
      "name" -> product.name,
      "description" -> product.description,
      "price" -> product.price,
      "mainImage" -> product.mainImage,
      "secondaryImages" -> product.secondaryImages,
      "qty" -> product.qty,
      "category" -> product.category,
      "porousAllowed" -> product.porousAllowed,
      "reviews" -> product.reviews
    )
  }


  def saveProductsForAnOrder(products: String): Unit ={
    /**String to Product*/
    var productsForAnOrder: Set[Product]  = Set.empty

    val newOrder: Array[String] = products.split("Product")
    for(p<-newOrder){
      val pro: Array[String] = p.split(",")
      if (pro.length == 9){
        productsForAnOrder += Product("","","","","","","","","","") //Product(pro.apply(0), pro.apply(1), pro.apply(2), pro.apply(3), pro.apply(4), pro.apply(5), pro.apply(6), pro.apply(7), pro.apply(8), pro.apply(9))
      }
    }

    val coll = db.collection[BSONCollection]("productsInOrders")

    /**Write to DB*/
    for (p<-productsForAnOrder){
      val key = BSONDocument(
        "productId" -> p.productId,
        "name" -> p.name,
        "description" -> p.description,
        "price" -> p.price,
        "mainImage" -> p.mainImage,
        "secondaryImages" -> p.secondaryImages,
        "qty" -> p.qty,
        "category" -> p.category,
        "porousAllowed" -> p.porousAllowed,
        "reviews" -> p.reviews
      )
      val futIns: Future[WriteResult] = coll.insert[BSONDocument](key)
      futIns.onComplete {
        case Failure(e) => throw e
        case Success(writeResult) =>
          println ("success writeResult "
          )
      }
    }


    //    for(i<-productsForAnOrder){
    //      println(i)
    //    }
  }

  //  products += (
  //    Product("0001","Paperclips Large","Large Plain Pack of 1000", "100", "images/page3_img1.jpg", "images/big1.jpg", "", "Lawnmower","")
  //    Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "100", "images/page3_img2.jpg", "images/big2.jpg", "", "Lawnmower",""),
  //    Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "100", "images/page3_img3.jpg", "images/big3.jpg", "", "Lawnmower",""),
  //    Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "100", "images/page3_img4.jpg", "images/big4.jpg", "", "Barbecues",""),
  //    Product("0005","Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img5.jpg", "images/big5.jpg", "", "Barbecues",""),
  //    Product("0006","AA", "No Tear Extra Large Pack of 1000", "100", "images/page3_img7.jpg", "images/big7.jpg", "", "Furniture",""),
  //    Product("0008","CC", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture",""),
  //    Product("0009","DD", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "", "Furniture",""),
  //    Product("0010","EE", "Zebra Length 28mm Assorted 150 Pack", "100", "images/page3_img8.jpg", "images/big8.jpg", "10", "Furniture","")
  //  )


  def getPrice(qty:String, price:String): Double ={
    val tPrice = (qty.toDouble) * (price.toDouble)
    tPrice
  }


  def findAll = products.toList.sortBy(_.name)


  def findByName(user: String) = products.find(_.name == user)


  //  def findByCart(cart: String) = products.find(_.category == cart).toList.sortBy(_.name)


  //find products by Category
  def findByCat(cat: String): List[Product] = {
    var tl: Set[Product]  = Set.empty
    for (product <- products){
      if (product.category == cat){
        tl += Product(product.productId, product.name, product.description,product.price, product.mainImage, product.secondaryImages, product.qty, product.category, product.porousAllowed, product.reviews)
      }
    }
    tl.toList
  }


  def preview(s: String, n: Int) = if (s.length <= n) { //takes a string to shorten and shortens up to length n, when there is a space.
    s
  } else {
    s.take(s.lastIndexWhere(_.isSpaceChar, n + 1)).trim
  }


  def findProductByName(name: String) = products.find(_.name == name)


  def add(Id: String, Name: String, description: String, price: String, mainImage: String, secondaryImages: String, need: String, category: String, porousAllowed: String, reviews: String): Unit ={
    products += Product(Id,Name,description,price,mainImage,secondaryImages, need, category, porousAllowed, reviews)

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
      add(tp.productId,tp.name,tp.description,tp.price,tp.mainImage,tp.secondaryImages,need,tp.category, tp.porousAllowed, tp.reviews)
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



