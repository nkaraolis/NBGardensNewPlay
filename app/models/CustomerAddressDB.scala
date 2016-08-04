package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerAddressDB(fullName: String, addressType: String, line1: String, line2: String, townCity: String, county: String, postcode: String)

object CustomerAddressDB {
  
  /*implicit object CustomerAddressDBReader extends BSONDocumentReader[CustomerAddressDB] {
    def read(doc: BSONDocument):
    CustomerAddressDB = CustomerAddressDB(
      doc.getAs[String]("fullName").get,
      doc.getAs[String]("addressType").get,
      doc.getAs[String]("line1").get,
      doc.getAs[String]("line2").get,
      doc.getAs[String]("townCity").get,
      doc.getAs[String]("county").get,
      doc.getAs[String]("postcode").get
    )
  }

  implicit object CustomerAddressWriter extends BSONDocumentWriter[CustomerAddressDB] {
    def write(customerAddress: CustomerAddressDB): BSONDocument =
      BSONDocument(
        "firstName" -> customerAddress.fullName,
        "type" -> customerAddress.addressType,
        "line1" -> customerAddress.line1,
        "line2" -> customerAddress.line2,
        "townCity" -> customerAddress.townCity,
        "county" -> customerAddress.county,
        "postcode" -> customerAddress.postcode
      )
  }*/

  /** Creates the reader and writer for the CustomerDB case class, ONE LINE FUCK ME */
  implicit val addressBSONHandler = Macros.handler[CustomerAddressDB]

  /** Update customer address **/
  def updateAddress(username: String, value: CustomerAddressDB, updater: String): Unit = {
    // Finds the user to update
    val selector = BSONDocument("username" -> username)
    // Sets the field to update to be addresses
    val modifier = BSONDocument(
      updater -> BSONDocument(
        "addresses" -> value))
    // Runs the update query
    val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
    runUpdate onComplete {
      Success =>
        println("Address updated")
    }
    Thread.sleep(500)
  }
}