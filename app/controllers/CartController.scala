package controllers

import java.util.Calendar

import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
  * Created by Administrator on 08/07/2016.
  */

class CartController extends Controller {

  // a form contents information about a product in the cart
  val CartForm: Form[aFormForCart] = Form(
    mapping(
      "Product" -> of[String],
      "Qty" -> of[String],
      "sTotal" -> of[String]
    )
    (aFormForCart.apply)
    (aFormForCart.unapply)
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


  def add(product: String, qty: String) = Action {
    implicit request =>  //controller action

      // find the product in all products
      val p = Product.findByName(product).get

      val quantity = qty.toInt

      //create a new cart item
      var cartItem = CartItem(p.productId.toInt, p.name, qty.toInt, p.price.toDouble)

      // update products in cart
      products =  Cart.addToCart(cartItem) //get product from model
      Redirect(routes.BrowseController.productList(p.category)) //render view template
  }


  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Cart.findByName(product).get)   //get product via Product class
      removeOldPrice(Product.findByName(product).get.name)
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }


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
      var newCartItem = CartItem(p.productId.toInt, p.name, q.toInt, p.price.toDouble)
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

      val quantity = q.toInt

      val subTot = (q.toDouble) * (p.price.toDouble)

      //create a new cart item and add to cart
      var newCartItem = CartItem(p.productId.toInt, p.name, q.toInt, p.price.toDouble)
      Cart.addToCart(newCartItem)

      //products = cp //get product from model
      products =  products :+ newCartItem
      totalT += subTot
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
    products = Cart.removeFromCart(CartItem(p.productId.toInt, p.name, p.qty.toInt, p.price.toDouble))  //get product from model

    products
  }


  def removeOldPrice(product: String) : Double ={
    totalT = totalT - (Product.findByName(product).get.price.toDouble * Product.findByName(product).get.qty.toDouble)
    totalT
  }





}
