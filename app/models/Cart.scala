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


  def convertToCartItems2(): Array[CartItem] = {
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


  def convertToCartItems(products: String): Array[CartItem] = {
    val newOrder: Array[String] = products.split("Product")
    val productsArray: Array[Product] = new Array[Product](products.length)
    var counter = 0

    for(p<-newOrder){
      val pro: Array[String] = p.split(",")
      if (pro.length == 9) {
        val p = Product.findByName(pro(1).toString)
        val product = Product(p.get.productId, p.get.name, p.get.description, p.get.price, p.get.mainImage, p.get.secondaryImages, p.get.qty, p.get.category, p.get.porousAllowed, p.get.reviews)

      //this might create too many products
      productsArray(counter) = product
      counter+1
      }
    }

    //val listOfItems = this.findAllInCart
    var cartItems: Array[CartItem] = new Array[CartItem](products.length)
    var counter2 = 0

    for (i <- productsArray){
      val ci = CartItem(i.productId.toInt, i.name, i.qty.toInt, i.price.toDouble)//this is now throwing an error
      cartItems(counter2) = ci
      counter2+1
    }
    cartItems





    //val newOrder: Array[String] = products.split("Product")
   // for(p<-newOrder){
   //   val pro: Array[String] = p.split(",")
    //  if (pro.length == 9){
    //    productsForAnOrder += Product("","","","","","","","","","") //Product(pro.apply(0), pro.apply(1), pro.apply(2), pro.apply(3), pro.apply(4), pro.apply(5), pro.apply(6), pro.apply(7), pro.apply(8), pro.apply(9))
     // }
    //}



  }





}
