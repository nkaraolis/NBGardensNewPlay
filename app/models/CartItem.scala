package models


import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import scala.util.{Failure, Success}

/**
  * Created by Administrator on 02/08/2016.
  */
case class CartItem (proId: Int, proName: String, quantity: Int, unitPrice:Double)


object CartItem {


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



}

