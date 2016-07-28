package models

import play.api.data.Forms._
import play.api.data._

/**
  * Created by Administrator on 06/07/2016.
  */
case class CustomerAddress(var username : String, var addressType: String, var fullName: String, var addressLine1: String, var addressLine2: String, var townCity: String, var county: String, var  postCode: String) {

}

object CustAddress {
  var custAddresses = Set(CustomerAddress("BenCosford", "Shipping", "Ben Cosford", "5th Floor Anchorage 1", "Anchorage Quay", "Salford Quays", "Greater Manchester", "M50 3YJ"),
                            CustomerAddress("BenCosford","Billing", "Ben Cosford", "4th Floor Anchorage 1", "Anchorage Quay", "Salford Quays", "Greater Manchester", "M50 3YJ"))

  def add(customerAddress: CustomerAddress): Unit = {
    custAddresses = custAddresses + customerAddress
  }

  def findAddressesByUsername(username: String) = custAddresses.filter(_.username == username)

  def getAddressByType(addresses: Set[CustomerAddress], input: String) = addresses.find(_.addressType == input)

}
