package models

/**
  * Created by Administrator on 08/08/2016.
  */

//This case class is to collect which card and which method the user chooses when paying for products
case class PaymentDetails(cardNumber: String, method: String)


