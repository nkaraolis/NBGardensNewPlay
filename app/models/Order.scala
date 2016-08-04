package models

/**
  * Created by Administrator on 21/07/2016.
  */
case class Order (val ordId:Int, val cusId:String, val carts: Array[Cart], val totalPrice: Double, val datetime: String, var status: String, var payMethod:String)


object Order{

  var orders : Set[Order] = Set.empty

  //def findOrderById(id:Int) = DataDump.orders.find(_.ordId == id)
  

  def getOrders = orders.toList

  //Method to get all orders with the logged in Customer's ID
 // def getOrdersByCusId(cid: String):List[Order] = DataDump.orders.filter(_.cusId == cid)



}
