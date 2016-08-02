package models

/**
  * Created by Administrator on 02/08/2016.
  */
case class CartItem (proId: Int, proName: String, quantity: Int, unitPrice:Double)


object CartItem {




  var cartitems: Set[CartItem]  = Set.empty


  def add(id: Int, name: String, quantity: Int, price: Double): Unit ={
    cartitems += CartItem(id,name,quantity,price)
  }

  def findByName(user: String) = cartitems.find(_.proName == user)



  def removeFromProduct(product: CartItem): Set[CartItem] ={
    def checkOldList(productsOld: Array[CartItem], product: CartItem): Array[CartItem] = {
      if (productsOld.isEmpty) {
        productsOld
      }
      else if (product.proId == productsOld.head.proId) {
        checkOldList(productsOld.tail, product)
      }
      else {
        checkOldList(productsOld.tail, product) :+ productsOld.head
      }
    }
    cartitems = checkOldList(cartitems.toArray, product).toSet
    cartitems
  }





}