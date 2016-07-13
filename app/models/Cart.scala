package models

/**
  * Created by Administrator on 12/07/2016.
  */
case class Cart (products: Array[Product])

object Cart{

  var productsInCart: Array[Product] = Array.empty

  def addToCart(product: Product): Array[Product] ={
    productsInCart = productsInCart :+ product
    productsInCart
  }

  def removeFromCart(product: Product): Array[Product] ={

    def checkCart(productsIn: Array[Product], product: Product): Array[Product] = {

      if (productsIn.isEmpty) {
        productsIn
      }
      else if (product.productId == productsIn.head.productId) {
        checkCart(productsIn.tail, product)
      }
      else {
        checkCart(productsIn.tail, product) :+ productsIn.head
      }
    }
    productsInCart = checkCart(productsInCart, product)
    productsInCart
  }
}