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
case class Product (productId: Int, name: String, description: String, price: String, mainImage: String, secondaryImages: String, qty: Int, category: String, porousAllowed: String, var reviews: List[Review])


object Product{

  //DB Connection
  val dbn = "NBGardensProducts"
  val user = "productAdmin"
  val pass = "1234"
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


  val productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()


  productsInDB.onComplete {
    case Failure(e) => throw e
    case Success(readResult) =>
      for (prod <- readResult) {
        products += productReader.read(prod)
      }
  }



  var loadCheck = false

  def loadedProducts(): Unit ={
    loadCheck = true
  }

  def loadProducts(){
    var productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()
    productsInDB.onComplete {
      case Failure(e) => throw e
        println("not ready yet main")
      case Success(readResult) =>
        for (prod <- readResult) {
          products += productReader.read(prod)
          println("ready main")
          loadedProducts()
          println(loadCheck)
        }
    }
  }

  def loadUpdatedProducts(): Unit ={

    var productsInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()

    products = Set.empty
    println(products.size)

    productsInDB.onComplete {
      case Failure(e) => throw e
        println("not ready yet main")
      case Success(readResult) =>
        for (prod <- readResult) {
          products += productReader.read(prod)
          println("ready main")
          loadedProducts()
          println(loadCheck)
        }
    }
    Thread.sleep(200)
  }


  def getImage(id: Int): String ={
    val imageStr = findById(id).get.mainImage
    imageStr
  }


  implicit object productReader extends BSONDocumentReader[Product]{
    def read(doc: BSONDocument):Product = Product(
      doc.getAs[Int]("productId").get,
      doc.getAs[String]("name").get,
      doc.getAs[String]("description").get,
      doc.getAs[String]("price").get,
      doc.getAs[String]("mainImage").get,
      doc.getAs[String]("secondaryImages").get,
      doc.getAs[Int]("qty").get,
      doc.getAs[String]("category").get,
      doc.getAs[String]("porousAllowed").get,
      doc.getAs[List[Review]]("reviews").get)
  }


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
        productsForAnOrder += Product(0,"","","","","",0,"","",List[Review]())
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


  }



  def getPrice(qty:Int, price:Double): Double ={
    val tPrice = "%.2f".format(qty.toDouble * price)
    tPrice.toDouble
  }

  def findAll = products.toList.sortBy(_.name)


  def findByName(name: String) = products.find(_.name == name)


  def findById(productId: Int) = products.find(_.productId == productId)


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


  //def findProductByName(name: String) = products.find(_.name == name)


  def add(Id: Int, Name: String, description: String, price: String, mainImage: String, secondaryImages: String, need: Int, category: String, porousAllowed: String, reviews: List[Review]): Unit ={
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
      val quantity = need.toInt
      val tp = products.toList.find(_.name == name).get
      removeFromProduct(tp)
      add(tp.productId,tp.name,tp.description,tp.price,tp.mainImage,tp.secondaryImages,quantity,tp.category, tp.porousAllowed, tp.reviews)
    }
    findByName(name)
  }


  //method used by the SearchController
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

  //method used by the SearchController
  def findByNameS(name: String) = products.toList.find(_.name.toLowerCase().contains(name.toLowerCase()))
}



