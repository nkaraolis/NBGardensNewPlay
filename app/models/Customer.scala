package models

/**
  * Created by Administrator on 07/07/2016.
  */

case class Customer(var Email:String, var password:String) {

}

object Customer {
  var customers = Set(Customer("john@gmail.com", "password"),
    Customer( "joe@gmail.com", "password"),
    Customer("fred@gmail.com",  "password"),
    Customer( "jane@gmail.com",  "password"))

  def findCustomer(cusId: String) = customers.find(_.Email == cusId)

}






