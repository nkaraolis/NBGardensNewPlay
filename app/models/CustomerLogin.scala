package models

/**
  * Created by Administrator on 07/07/2016.
  */

case class CustomerLogin(var Email:String, var password:String) {

}

object CustomerLogin {
  var customers = Set(CustomerLogin("john@gmail.com", "password"),
    CustomerLogin( "joe@gmail.com", "password"),
    CustomerLogin("fred@gmail.com",  "password"),
    CustomerLogin( "jane@gmail.com",  "password"))

  def findCustomer(cusId: String) = customers.find(_.Email == cusId)

}






