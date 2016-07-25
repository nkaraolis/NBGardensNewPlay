package models

/**
  * Created by Administrator on 21/07/2016.
  */
case class Order (val ordId:Int, val cusId:String, val carts: Array[Cart], val datetime: String, var status: String, var payMethod:String){
  

}
