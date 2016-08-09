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
import models.CustomerCardDB


/**
  * Created by Administrator on 29/07/2016.
  */
case class OrderDB (ordId:Int, cusId:String, carts: Array[CartItem], totalPrice: Double, datetime: String, status: String, selectedCardNo: String, payMethod:String)

object OrderDB {

  var orderset = Set.empty(OrderDBReader)
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
        orders += OrderDBReader.read(ord)
      }
  }



  implicit object OrderDBReader extends BSONDocumentReader[OrderDB]{
    def read(doc: BSONDocument):OrderDB =
      OrderDB(
      doc.getAs[Int]("ordId").get,
      doc.getAs[String]("cusId").get,
      doc.getAs[Array[CartItem]]("carts").get,
      doc.getAs[Double]("totalPrice").get,
      doc.getAs[String]("datetime").get,
      doc.getAs[String]("status").get,
      doc.getAs[String]("selectedCardNo").get,
      doc.getAs[String]("payMethod").get)
  }



  implicit object OrderDBWriter extends BSONDocumentWriter[OrderDB] {
      def write(order: OrderDB): BSONDocument = BSONDocument(
          "ordId" -> order.ordId,
          "cusId" -> order.cusId,
          "carts" -> order.carts,
          "totalPrice" -> order.totalPrice,
          "datetime" -> order.datetime,
          "status" -> order.status,
          "selectedCard" -> order.selectedCardNo,
          "payMethod" -> order.payMethod
        )
  }




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

    val cDay = MonthDay.now()
    val cMonth = YearMonth.now()
    val cYear = Year.now()
    val time = LocalTime.now()

    val datetime = cDay + " " + cMonth + " " + cYear + " " + time
    datetime
  }


  def findOrderById(id: Int) = orders.find(_.ordId == id)

  def getOrders = orders.toList

  //Method to get all orders with the logged in Customer's ID
  def getOrdersByCusId(cid: String): List[OrderDB] = orders.filter(_.cusId == cid).toList



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
