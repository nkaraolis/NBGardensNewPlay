package controllers

import play.api.mvc._
import models.{Cart, Product}


class CartController extends Controller {

  def list = Action {
    implicit request =>  //controller action
      val products = Cart.productsInCart  //get product from model
      Ok(views.html.cartpage(products.toList)) //render view template
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
      Ok(views.html.cartpage(products.toList)) //render view template
  }
}
