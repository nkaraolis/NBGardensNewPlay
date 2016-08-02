package models

/**
  * Created by Administrator on 12/07/2016.
  */
//case class Cart (products: Array[Product])

case class Cart (cartItems: Array[CartItem])


object Cart {

  var productsInCart: Array[CartItem] = Array.empty

  def addToCart(product: CartItem): Array[CartItem] = {
    productsInCart = productsInCart :+ product
    productsInCart
  }


  def removeFromCart(product: CartItem): Array[CartItem] = {
    def checkCart(productsIn: Array[CartItem], product: CartItem): Array[CartItem] = {
      if (productsIn.isEmpty) {
        productsIn
      }
      else if (product.proId == productsIn.head.proId) {
        checkCart(productsIn.tail, product)
      }
      else {
        checkCart(productsIn.tail, product) :+ productsIn.head
      }
    }
    productsInCart = checkCart(productsInCart, product)
    productsInCart
  }


  def findAllInCart = productsInCart.toList.sortBy(_.proName)


//  def calculateCartTotal(products: Array[Product]): Double = {
//    var total: Double = 0.00
//    for (pro <- products) {
//      var qty = pro.qty.toDouble
//      var price = pro.price.toDouble
//      total += qty * price
//    }
//    total
//  }


  def calculateCartTotal(cartItems: Array[CartItem]): Double = {
    var total: Double = 0.00
    for (pro <- cartItems) {
      var qty = pro.quantity.toDouble
      var price = pro.unitPrice.toDouble
      total += qty * price
    }
    total
  }



}
