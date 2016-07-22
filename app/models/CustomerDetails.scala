package models

import play.api.data.Forms._
import play.api.data._

/**
  * Created by Administrator on 06/07/2016.
  */
case class CustomerDetails(var firstName: String, var lastName: String, var email: String, var telephone: String, var  username: String, var password: String) {

}

object Customer {
  var customers = Set(CustomerDetails("Nick", "Karaolis", "MrNoob@qa.com", "07704235798", "bigNoob", "password"),
    CustomerDetails("Tevyn", "Allen", "TevynAllen@qa.com", "07701326644", "punks", "password"), CustomerDetails("Ben", "Cosford", "ben@qa.com", "07706827695", "BenCosford", "password"))

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
