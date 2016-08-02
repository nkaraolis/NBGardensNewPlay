package models

import java.util.Calendar
import com.typesafe.config.ConfigFactory
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import reactivemongo.core.nodeset.Authenticate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


/**
  * Created by Administrator on 29/07/2016.
  */
case class OrderDB (ordId:Int, cusId:String, carts: Array[Product], totalPrice: Double, datetime: String, status: String, payMethod:String)

object OrderDB {

  var orderset = Set.empty(OrderReader)

  //DB Connection
  val dbn = "NBGardensOrders"
  val user = "user2"
  val pass = "password"
  val creds = List(Authenticate(dbn, user, pass))
  val servs: List[String] = List("192.168.1.42:27017")
  val config = ConfigFactory.load()
  val driver = new MongoDriver(Some(config))
  val conn = driver.connection(servs, authentications = creds)
  val strat = FailoverStrategy()
  val db = conn.db(dbn, strat)
  val coll = db.collection[BSONCollection]("order")

  var orders: Set[OrderDB] = Set.empty

  val condition = BSONDocument(
    (null, null)
  )

  val key = BSONDocument(
    ("_id" -> false)
  )


  val ordersInDB = coll.find(condition, key).cursor[BSONDocument]().collect[List]()

  ordersInDB.onComplete {
    case Failure(e) => throw e
    case Success(readResult) =>
      for (ord <- readResult) {
        orders += OrderReader.read(ord)
      }
  }


  implicit object OrderReader extends BSONDocumentReader[OrderDB] {
    def read(doc: BSONDocument): OrderDB = OrderDB(
      doc.getAs[Int]("ordId").get,
      doc.getAs[String]("cusId").get,
      doc.getAs[Array[Product]]("carts").get,
      doc.getAs[Double]("totalPrice").get,
      doc.getAs[String]("datetime").get,
      doc.getAs[String]("status").get,
      doc.getAs[String]("payMethod").get)
  }


  //  implicit object OrderWriter extends BSONDocumentWriter[OrderDB] {
  //    def write(order: OrderDB) : BSONDocument = BSONDocument(
  //      "ordId" -> order.ordId,
  //      "cusId" -> order.cusId,
  //      "carts" -> order.carts,
  //      "totalprice" -> order.totalPrice,
  //      "datetime" -> order.datetime,
  //      "status" -> order.status,
  //      "payMethod" -> order.payMethod
  //    )
  //  }


  def createDoc(orderd: OrderDB): BSONDocument = {
    val document = BSONDocument(
      "ordId" -> orderd.ordId,
      "cusId" -> orderd.cusId,
      "carts" -> orderd.carts, //Array - cant do this
      "totalprice" -> orderd.totalPrice,
      "datetime" -> orderd.datetime,
      "status" -> orderd.status,
      "payMethod" -> orderd.payMethod
    )
    document
  }

  def insertDoc(collection: BSONCollection, doc: BSONDocument): Future[Unit] = {
    val writeResult: Future[WriteResult] = collection.insert(doc)
    Thread.sleep(2000)

    writeResult.onComplete {
      case Failure(e) => e.printStackTrace()
      case Success(writeResult) =>
        println("SUCCESS")
    }
    writeResult.map(_ => {})
  }

  //def create (ordercol: BSONCollection, order: OrderDB)(implicit ec: ExecutionContext): Future[Unit] = {
  //  val result = ordercol.insert(order)
  //  result.map(_ => {})
  //}

  def getDateTime(): String = {
    val now = Calendar.getInstance().toString

    now
  }


  def createNewOrder(id: Int, cId: String, carts: Array[Product], payMethod: String) {

    val now = Calendar.getInstance()
    val status = "Order Made"
    val total: Double = Cart.calculateCartTotal(carts)
    var doc = BSONDocument("orderID" -> id, "customerID" -> cId, "items" -> carts, "totalprice" -> total, "datetime" -> now.toString(), "status" -> status, "payMethod" -> payMethod)
    val futIns: Future[WriteResult] = MongoConnector.collectionOrder.insert[BSONDocument](doc)

    futIns.onComplete {
      case Failure(e) => throw e
      case Success(writeResult) => println(s"success: $writeResult")
    }

    val end: Future[Unit] = futIns.map {
      case writeResult if (writeResult.code contains 11000) => println("Warning 11000: Duplicate")
      case _ => ()
    }

    //need to trigger decrement stock

  }


  def findOrderById(id: Int) = DataDump.orders.find(_.ordId == id)


  def getOrders = orders.toList

  //Method to get all orders with the logged in Customer's ID
  def getOrdersByCusId(cid: String): List[Order] = DataDump.orders.filter(_.cusId == cid)

}
