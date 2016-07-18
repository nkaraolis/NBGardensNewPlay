package models

import play.api.data.Forms._
import play.api.data._

/**
  * Created by Administrator on 06/07/2016.
  */
case class CustomerDetails(var firstName: String, var lastName: String, var email: String, var telephone: String, val username: String, var password: String) {

}

object Customer {
  var customers = Set(CustomerDetails("Nick", "Noob", "MrNoob@everywhere.com", "999", "NickKaraolis", "mrPassword"),
    CustomerDetails("Jay", "Noob", "JayNoob@everywhere.com", "999", "TevynAllen", "mrPassword"), CustomerDetails("Ben", "Cosford", "ben@everywhere.com", "999", "BenCosford", "mrPassword"))

  def add(customer: CustomerDetails): Unit = {
    customers = customers + customer
  }

  def findAllCustomer() = customers.toList.sortBy(_.username)

  def findByEmail(email: String) = customers.find(_.email == email)

  def findByUsername(username: String) = customers.find(_.username == username)

  def findCustomer(username : String) = customers.find(_.username == username).head

  def updateCustomers(username : String, newData : String): Unit = {

  }

}
