package models

/**
  * Created by Administrator on 12/07/2016.
  */
//case class Cart (products: Array[Product])

case class Cart (cartItems: Array[Product])


object Cart {

  var productsInCart: Array[Product] = Array.empty

  def addToCart(product: Product): Array[Product] = {
    productsInCart = productsInCart :+ product
    productsInCart
  }


  def removeFromCart(product: Product): Array[Product] = {
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


  def findAllInCart = productsInCart.toList.sortBy(_.name)


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
      var price = pro.unitPrice
      total += qty * price
    }
    total
  }


  def convertToCartItems(): Array[CartItem] = {
    val listOfItems = this.findAllInCart
    var cartItems: Array[CartItem] = new Array[CartItem](listOfItems.length)
    var counter = 0
    for (i <- listOfItems){

      val ci = CartItem(i.productId.toInt, i.name, i.qty.toInt, i.price.toDouble)
      cartItems(counter) = ci
      counter+1
    }
    cartItems

  }





}
