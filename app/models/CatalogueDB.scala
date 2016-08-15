package models

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

import scala.util.{Failure, Success}

/**
  * Created by Administrator on 28/07/2016.
  */
case class CatalogueDB(catalogueID: Int, name: String, email: String, line1: String, line2: String, town: String, postcode: String)

object CatalogueDB {
  var  catalogueList: List[BSONDocument] = List(BSONDocument())

  implicit object CatalogueDBReader extends BSONDocumentReader[CatalogueDB] {
    def read(doc: BSONDocument): CatalogueDB =
      CatalogueDB(
        doc.getAs[Int]("catalogueID").get,
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("line1").get,
        doc.getAs[String]("line2").get,
        doc.getAs[String]("town").get,
        doc.getAs[String]("postcode").get
      )
  }

  implicit object CatalogueWriter extends BSONDocumentWriter[CatalogueDB] {
    def write(catalogue: CatalogueDB): BSONDocument = {
      BSONDocument(
        "catalogueID" -> catalogue.catalogueID,
        "name" -> catalogue.name,
        "email" -> catalogue.email,
        "line1" -> catalogue.line1,
        "line2" -> catalogue.line2,
        "town" -> catalogue.town,
        "postcode" -> catalogue.postcode
      )
    }
  }

  def findNextID(): Int = {
    var nextID = 0
    val findAll = BSONDocument(
      (null, null)
    )
    val foundID = MongoConnector.collectionCatalogue.find(findAll).cursor[BSONDocument]().collect[List]()

    foundID onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        nextID = readResult.size + 1
    }
    Thread.sleep(500)
    nextID
  }


}