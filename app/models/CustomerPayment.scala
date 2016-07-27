package models

import play.api.data.Forms._
import play.api.data._

/**
  * Created by Administrator on 06/07/2016.
  */
case class CustomerPayment(var username : String, var paymentMethod: String, var nameOnCard: String, var cardNumber: String, var startDate: String, var expiryDate: String, var securityCode: String, var issueNo: String) {

}

object CustPayment {
  var custPayments = Set(CustomerPayment("BenCosford", "Debit", "Ben Cosford", "1234", "26/07/2015", "26/07/2018", "123", ""),
    CustomerPayment("BenCosford","Credit", "Ben Cosford", "5678", "26/07/2015", "26/07/2017", "456", "123"),
    CustomerPayment("BenCosford","Paypal", "", "", "", "", "", ""))

  def add(customerPayment: CustomerPayment): Unit = {
    custPayments = custPayments + customerPayment
  }

  def findPaymentsByUsername(username: String) = custPayments.filter(_.username == username)

  def getPaymentByMethod(payments: Set[CustomerPayment], input: String) = payments.find(_.paymentMethod == input)

}
