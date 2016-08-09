package models

import com.typesafe.config.ConfigFactory
import reactivemongo.api.{FailoverStrategy, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by Administrator on 02/08/2016.
  */
case class CartItem (proId: Int, proName: String, quantity: Int, unitPrice:Double,var porousRequired: Boolean)


object CartItem {

  /** Creates the reader and writer for the CartItem case class */
  implicit val customerBSONHandler = Macros.handler[CartItem]
}

