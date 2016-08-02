package models

import java.util.Calendar
import java.text.SimpleDateFormat
import java.time._

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
  var orders: Set[OrderDB]  = Set.empty

  val condition = BSONDocument(
    (null, null)
  )

  val key = BSONDocument(
    ("_id" -> false)
  )


  val ordersInDB = MongoConnector.collectionOrder.find(condition, key).cursor[BSONDocument]().collect[List]()

  ordersInDB.onComplete {
    case Failure(e) => throw e
    case Success(readResult) =>
      for (ord <- readResult) {
        orders += OrderReader.read(ord)
      }
  }



  implicit object OrderReader extends BSONDocumentReader[OrderDB]{
    def read(doc: BSONDocument):OrderDB = OrderDB(

      doc.getAs[Int]("ordId").get,
      doc.getAs[String]("cusId").get,
      doc.getAs[Array[Product]]("carts").get,
      doc.getAs[Double]("totalPrice").get,
      doc.getAs[String]("datetime").get,
      doc.getAs[String]("status").get,
      doc.getAs[String]("payMethod").get)
  }



  implicit object OrderWriter extends BSONDocumentWriter[OrderDB] {
      def write(order: OrderDB) : BSONDocument = BSONDocument(
        "ordId" -> order.ordId,
        "cusId" -> order.cusId,
        "carts" -> order.carts,
        "totalprice" -> order.totalPrice,
        "datetime" -> order.datetime,
        "status" -> order.status,
        "payMethod" -> order.payMethod
      )
  }


  def createDoc(orderd: OrderDB): BSONDocument = {
    val document = BSONDocument(
      "ordId" -> orderd.ordId,
      "cusId" -> orderd.cusId,
      "carts" -> orderd.carts,
      "totalprice" -> orderd.totalPrice,
      "datetime" -> orderd.datetime,
      "status" -> orderd.status,
      "payMethod" -> orderd.payMethod
    )
    document
  }
//
//
//  def insertDoc(collection: BSONCollection, doc: BSONDocument): Future[Unit] = {
//    val writeResult: Future[WriteResult] = collection.insert(doc)
//    Thread.sleep(2000)
//
//    writeResult.onComplete {
//      case Failure(e) => e.printStackTrace()
//      case Success(writeResult) =>
//        println("SUCCESS")
//    }
//    writeResult.map(_ => {})
//  }



  def getDateTime(): String = {
    val now = Calendar.getInstance()

    // create the date/time formatters
    val minuteFormat = new SimpleDateFormat("mm")
    val hourFormat = new SimpleDateFormat("hh")
    val amPmFormat = new SimpleDateFormat("a")

   // val cHour = hourFormat.format(now)
   // val cMin = minuteFormat.format(now)
    //val amOrPm = amPmFormat.format(now)

    val cDay = MonthDay.now()
    val cMonth = YearMonth.now()
    val cYear = Year.now()
    val time = LocalTime.now()

    //val datetime = cDay + " " + cMonth + " " + cYear + " " + cHour + ":" + cMin + " " + amOrPm

    val datetime = cDay + " " + cMonth + " " + cYear + " " + time
    datetime
  }


//  def createOrder (sortedOrders: List[OrderDB] ,cart : Array[Product], total: Double,status : String,  username: String, payMethod : String) = {
//    if (sortedOrders.isEmpty)
//      {
//       new OrderDB(1, username, cart, total, this.getDateTime(), status, payMethod)
//      }else
//    {
//      new OrderDB(sortedOrders.last.ordId + 1, username, cart, total, this.getDateTime(), status, payMethod)
//    }
//
//  }
//
//
//
//  def createNewOrder(id: Int, cId: String, carts: Array[Product], payMethod: String) {
//    val now = Calendar.getInstance()
//    val status = "Order Made"
//    val total: Double = Cart.calculateCartTotal(carts)
//    var doc = BSONDocument("orderID" -> id, "customerID" -> cId, "items" -> carts, "totalprice" -> total, "datetime" -> now.toString(), "status" -> status, "payMethod" -> payMethod)
//    val futIns: Future[WriteResult] = MongoConnector.collectionOrder.insert[BSONDocument](doc)
//
//    futIns.onComplete {
//      case Failure(e) => throw e
//      case Success(writeResult) => println(s"success: $writeResult")
//    }
//
//    val end: Future[Unit] = futIns.map {
//      case writeResult if (writeResult.code contains 11000) => println("Warning 11000: Duplicate")
//      case _ => ()
//    }
//    //need to trigger decrement stock
//
//  }


  def findOrderById(id: Int) = DataDump.orders.find(_.ordId == id)

  def getOrders = orders.toList

  //Method to get all orders with the logged in Customer's ID

  def getOrdersByCusId(cid: String): List[Order] = DataDump.orders.filter(_.cusId == cid)



  def findNextID(): Int = {
    var nextID = 0

    val findAll = BSONDocument(
      (null, null)
    )

    val foundID = MongoConnector.collectionOrder.find(findAll).cursor[BSONDocument]().collect[List]()

    foundID onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        nextID = readResult.size + 1
    }
    Thread.sleep(500)
    nextID
  }
}
