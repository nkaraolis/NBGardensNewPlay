package models

/**
  * Created by Administrator on 12/07/2016.
  */

case class Cart (cartItems: Array[Product])


object Cart {

  var productsInCart: Array[CartItem] = Array.empty


  def clearCart(): Unit ={

    productsInCart = Array.empty
  }

  def addToCart(product: CartItem): Array[CartItem] = {
    productsInCart = productsInCart :+ product
    productsInCart
  }

  def findByName(name: String) = productsInCart.find(_.proName == name)


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


  def calculateCartTotal(): Double = {
    var total: Double = 0.00
    for (pro <- productsInCart) {
      var qty = pro.quantity.toDouble
      var price = pro.unitPrice
      total += qty * price
    }
    total
  }


}
