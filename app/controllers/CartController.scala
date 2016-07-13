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

  def add(product: String) = Action {
    implicit request =>  //controller action
      products :+ Cart.addToCart(Product.findByName(product).get)  //get product from model
      Redirect(routes.BrowseController.list) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      Ok(views.html.cartpage(products.toList)) //render view template
  }
}
