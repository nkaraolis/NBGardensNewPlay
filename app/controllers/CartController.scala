package controllers

import java.util.Calendar

import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.i18n.Messages

/**
  * Created by Administrator on 08/07/2016.
  */

class CartController extends Controller {

  // a form contents information about a product in the cart
  val CartForm = Form(
    tuple(
      "Product" -> of[String],
      "Qty" -> of[String],
      "sTotal" -> of[String],
      "pwCheck" -> nonEmptyText
    )
  )


  // products in the cart for the user
  var products: Array[CartItem] = Array.empty
  var totalT = 0.00


  // return products in the cart
  def list = Action {
    implicit request =>  //controller action
      products = Cart.productsInCart  //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def findByName(name: String) = products.find(_.proName == name)

  def add(product: String, qty: String) = Action {
    implicit request =>  //controller action

      Redirect(routes.BrowseController.productList("")) //render view template
  }


  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Cart.findByName(product).get)   //get product via Product class
      removeOldPrice(Product.findByName(product).get.name)
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def updatePW() = Action {
    implicit request =>  //controller action

      val product:String = CartForm.bindFromRequest().data("Product")

      var productToUpdate = findByName(product).get

      println("product to update is " + productToUpdate.proName + ". It's status before is " + productToUpdate.porousRequired)

      if(productToUpdate.porousRequired){
        productToUpdate.porousRequired = false
      }else{
        productToUpdate.porousRequired = true
      }

      println(productToUpdate.proName + ". It's status now is " + productToUpdate.porousRequired)

      // calculate the new total for the product
      val subTot = productToUpdate.quantity.toDouble * productToUpdate.unitPrice

      // remove the out of date product from the cart list
      removeO(product)
      removeOldPrice(Product.findByName(product).get.name)

      // update products in cart
      Cart.addToCart(productToUpdate)

      products = products :+ productToUpdate
      totalT += subTot

      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  /**for(cartItem <- products) {

    println(Product.findById(cartItem.proId).get.porousAllowed)

    if(Product.findById(cartItem.proId).get.porousAllowed == "true"){

      val pwCheck: String = CartForm.bindFromRequest().data("pwCheck")

      if(pwCheck == "true"){
        cartItem.porousRequired = true
      }else {
        cartItem.porousRequired = false
      }
    }
  }**/


  // update qty from the cart page
  def update() = Action {
    implicit request =>  //controller action

      // get the current product in the cart which the user wants to change
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get

      // get the new qty for the product
      val q:String = CartForm.bindFromRequest().data("Qty")

      val quantity = q.toInt

      // calculate the new total for the product
      val subTot = (q.toDouble) * (p.price.toDouble)

      // call the renewTotalPrice function to have a new total price for the user (see renewTotalPrice)
      removeOldPrice(p.name)

      // remove the out of date product from the cart list
      removeO(p.name)

      //create a new cart item and add to cart
      var newCartItem = CartItem(p.productId.toInt, p.name, q.toInt, p.price.toDouble,false)
      Cart.addToCart(newCartItem)

      // update products in cart
      products =  products :+ newCartItem //get product from model
      totalT += subTot
      Ok(views.html.cartpage(products.toList, CartForm))//render view template

  }

  // update qty from the products page, the same with above function
  def updateFromPL() = Action {
    implicit request =>  //controller action
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get
      val q:String = CartForm.bindFromRequest().data("Qty")

      val qty = q.toInt

      val subTotalAdd = q.toDouble * p.price.toDouble

      //create a new cart item
      val cartItem = CartItem(p.productId, p.name, qty, p.price.toDouble,false)

      if(findByName(p.name).isEmpty){

        // update products in cart
        Cart.addToCart(cartItem) //get product from model
        //products = cp //get product from model
        products =  products :+ cartItem
        totalT += subTotalAdd

      } else{

        // find the product in the cart
        // update quantity

        val productToChange = findByName(p.name)

        val quantity = productToChange.get.quantity

        // calculate the new total for the product
        val subTotalUpdate = (quantity.toDouble + qty.toDouble) * productToChange.get.unitPrice

        // call the renewTotalPrice function to have a new total price for the user (see renewTotalPrice)
        removeOldPrice(productToChange.get.proName)

        // remove the out of date product from the cart list
        removeO(productToChange.get.proName)

        val updatedCartItem = CartItem(p.productId, p.name, qty.toInt + quantity, p.price.toDouble,false)
        Cart.addToCart(updatedCartItem)

        products =  products :+ updatedCartItem
        totalT += subTotalUpdate
      }
      Redirect(routes.BrowseController.productList(p.category))//render view template
  }

  def checkout() = Action {
    implicit request =>  //controller action

      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      } else {
        totalT = 0.00
        for (i<-products){
          totalT += (i.quantity.toDouble*i.unitPrice)
        }
        Ok(views.html.checkout(products.toList, totalT)) //render view template
      }
  }

  def removeO(product: String) : Array[CartItem] ={
    val p = Product.findByName(product).get
    products = Cart.removeFromCart(CartItem(p.productId.toInt, p.name, p.qty.toInt, p.price.toDouble,false))  //get product from model

    products
  }


  def removeOldPrice(product: String) : Double ={
    totalT = totalT - (Product.findByName(product).get.price.toDouble * Product.findByName(product).get.qty.toDouble)
    totalT
  }


  val PayDetailsForm = Form(tuple(
    "Method" -> of[String],
    "Card" -> of[String]

  ))


  //This method is to add a customer's payment card to their order
  def addCard(total:Double, cardNo:String) = Action {
    implicit request =>
      Ok(views.html.payPage(total, PayDetailsForm, cardNo)) //render the payPage view
  }



}
