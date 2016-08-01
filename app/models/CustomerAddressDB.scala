package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Administrator on 26/07/2016.
  */
case class CustomerAddressDB(addressType: String, line1: String, line2: String, county: String, postcode: String)

object CustomerAddressDB{
  implicit  object CustomerAddressDBReader extends  BSONDocumentReader[CustomerAddressDB]{
    def read(doc : BSONDocument) :
    CustomerAddressDB = CustomerAddressDB(
      doc.getAs[String]("addressType").get,
      doc.getAs[String]("line1").get,
      doc.getAs[String]("line2").get,
      doc.getAs[String]("county").get,
      doc.getAs[String]("postcode").get
    )
  }

  implicit object CustomerAddressWriter extends  BSONDocumentWriter[CustomerAddressDB]{
    def write(customerAddress: CustomerAddressDB): BSONDocument =
      BSONDocument(
        "type" -> customerAddress.addressType,
        "line1" -> customerAddress.line1,
        "line2" -> customerAddress.line2,
        "county" -> customerAddress.county,
        "postcode" -> customerAddress.postcode
      )
  }
}