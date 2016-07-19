package models

/**
  * Created by Administrator on 07/07/2016.
  */

case class CustomerLogin(username:String, password:String) {

}

object CustomerLogin {
  var customers = Set(CustomerLogin("NickKaraolis", "password"),
    CustomerLogin("TevynAllen", "password"),
    CustomerLogin("BenCosford",  "password"))

  def findCustomer(user: String) = customers.find(_.username == user)

}