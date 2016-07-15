package controllers

import play.api.mvc._
import models.{Cart, Product, aFormForCart}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._


class CartController extends Controller {

  val CartForm: Form[aFormForCart] = Form(mapping(
    "Product" -> of[String],
    "Qty" -> of[String])(aFormForCart.apply)(aFormForCart.unapply))

  def list = Action {
    implicit request =>  //controller action
      val products = Cart.productsInCart  //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  var products: Array[Product] = Array.empty

  def add(product: String, Qty: String) = Action {
    implicit request =>  //controller action
      val p = Product.findByName(product).get
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.Name, p.description, p.price, p.imgS, p.imgL, Qty)
        val t = Product.findByName(p.Name).toArray.apply(0)
        t
      }
      val cp = Cart.addToCart(np)
      products :+  cp //get product from model
      Redirect(routes.BrowseController.list) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def update(Qty: String) = Action {
    implicit request =>  //controller action
//      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }
}

//  def add(product: String) = Action {
//    implicit request =>  //controller action
//      val p = Product.findByName(product).get
//      val cp = Cart.addToCart(p)
//      products :+  cp //get product from model
//      Redirect(routes.BrowseController.list) //render view template
//  }