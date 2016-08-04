package models

/**
  * Created by Administrator on 12/07/2016.
  */
//case class Cart (products: Array[Product])

case class Cart (cartItems: Array[Product])


object Cart {

//  var productsInCart: Array[Product] = Array.empty
//
//  def addToCart(product: Product): Array[Product] = {
//    productsInCart = productsInCart :+ product
//    productsInCart
//  }


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


  def calculateCartTotal(): Double = {
    var total: Double = 0.00
    for (pro <- productsInCart) {
      var qty = pro.quantity.toDouble
      var price = pro.unitPrice
      total += qty * price
    }
    total
  }


//  def convertToCartItems2(): Array[CartItem] = {
//    val listOfItems = this.findAllInCart
//    var cartItems: Array[CartItem] = new Array[CartItem](listOfItems.length)
//    var counter = 0
//
//    for (i <- listOfItems){
//      val ci = CartItem(i.productId.toInt, i.name, i.qty.toInt, i.price.toDouble)
//      cartItems(counter) = ci
//      counter+1
//    }
//    cartItems
//
//  }


  def convertToCartItems3(products: String): Array[CartItem] = {
    val newOrder: Array[String] = products.split("Product")
    val productsArray: Array[Product] = new Array[Product](products.length)
    var counter = 0

    for(p <- newOrder){
      val pro: Array[String] = p.split(",")
      if (pro.length == 9) {
       val last = pro.last //no errors so there must be something in pro
        val pr = Product.findByName(pro.apply(1).toString)//array index out of bounds//doesnt happen when if block is uncommented
        val product = Product(pr.get.productId, pr.get.name, pr.get.description, pr.get.price, pr.get.mainImage, pr.get.secondaryImages, pr.get.qty, pr.get.category, pr.get.porousAllowed, pr.get.reviews)
        productsArray(counter) = product
        counter + 1
      }
    }

    //val listOfItems = this.findAllInCart
    var cartItems: Array[CartItem] = new Array[CartItem](products.length)
    var count = 0

    for (pa <- productsArray){
      val lasty = productsArray.last //this doesnt flag up any error
      val amount = productsArray.length
      //val ca = CartItem(amount.toInt, "Gnone", 12, 12.00) //it works with this
      val ci = CartItem(pa.productId.toInt, pa.name, pa.qty.toInt, pa.price.toDouble)//this is throwing a null pointer exception///////////////////////
      cartItems(count) = ci
      count + 1
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




  def convertToCartItems4(products: String): Array[CartItem] = {
    val newOrder: Array[String] = products.split("Product")    //.remove(",List()")
    print("Inside the convertToCartItems method: " + newOrder) //java.lang.String error
    val productsArray: Array[Product] = new Array[Product](newOrder.length)
    var counter = 0

    for (p <- newOrder) {
      val pro: Array[String] = p.split(",")
      val pr = Product.findByName(p.apply(counter + 1).toString)
      val product = Product(pr.get.productId, pr.get.name, pr.get.description, pr.get.price, pr.get.mainImage, pr.get.secondaryImages, pr.get.qty, pr.get.category, pr.get.porousAllowed, pr.get.reviews)
      productsArray(counter) = product
      counter + 10


//      if (pro.length == 10) {
//        val last = pro.last //no errors so there must be something in pro
//        //val pr = Product.findByName(pro.apply(1).toString) //array index out of bounds//doesnt happen when if block is uncommented
//        val product = Product(pr.get.productId, pr.get.name, pr.get.description, pr.get.price, pr.get.mainImage, pr.get.secondaryImages, pr.get.qty, pr.get.category, pr.get.porousAllowed, pr.get.reviews)
//
//      }
    }

    //val listOfItems = this.findAllInCart
    var cartItems: Array[CartItem] = new Array[CartItem](products.length / 10)
    var count = 0

    for (pa <- productsArray) {
      val lasty = productsArray.last //this doesnt flag up any error
      println("This is the last element in productsArray: " + lasty)
      val amount = productsArray.length
      //val ca = CartItem(amount.toInt, "Gnone", 12, 12.00) //it works with this
      val ci = CartItem(pa.productId.toInt, pa.name, pa.qty.toInt, pa.price.toDouble) //this is throwing a null pointer exception///////////////////////
      cartItems(count) = ci
      count + 1
    }
    cartItems
  }



//  def convertToCartItems(): Array[CartItem] = {
//
//    var cartItems: Array[CartItem] = new Array[CartItem](productsInCart.length)
//    var count = 0
//    for (p <- productsInCart) {
//      cartItems(count) = p
//      count + 1
//    }
//    cartItems
//  }



}
