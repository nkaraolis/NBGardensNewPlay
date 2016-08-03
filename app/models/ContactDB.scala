package models

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

import scala.util.{Failure, Success}

/**
  * Created by Administrator on 28/07/2016.
  */
case class ContactDB(contactID: Int, name: String, email: String, message: String)

object ContactDB {
  var  contactList: List[BSONDocument] = List(BSONDocument())

  implicit object ContactDBReader extends BSONDocumentReader[ContactDB] {
    def read(doc: BSONDocument): ContactDB =
      ContactDB(
        doc.getAs[Int]("contactID").get,
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("message").get
      )
  }

  implicit object ContactWriter extends BSONDocumentWriter[ContactDB] {
    def write(contact: ContactDB): BSONDocument = {
      BSONDocument(
        "contactID" -> contact.contactID,
        "name" -> contact.name,
        "email" -> contact.email,
        "message" -> contact.message
      )
    }
  }

  def findNextID(): Int = {
    var nextID = 0
    val findAll = BSONDocument(
      (null, null)
    )
    val foundID = MongoConnector.collectionContact.find(findAll).cursor[BSONDocument]().collect[List]()

    foundID onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        nextID = readResult.size + 1
    }
    Thread.sleep(500)
    nextID
  }

}