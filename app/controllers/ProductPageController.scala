package controllers

import play.api.mvc._
import models.{Cart, Product}
import play.api.mvc.{Action, Controller}
import models.Product


class ProductPageController extends Controller {

  def goToProduct(product: String) = Action {
    implicit request =>  //controller action

      var clickedProduct = Product.findProductByName(product).get

      Ok(views.html.productPage(clickedProduct))

  }

  def leaveReview = Action {
    implicit request =>
      if(request.session.get("username").isEmpty){
        Redirect(routes.LoginController.newLogin())
      } else {
        null
      }

  }

  //def submitReview = Action {
    //implicit request =>

  //}

  /**var products: Array[Product] = Array.empty

  def add(product: String, Qty: String) = Action {
    implicit request =>  //controller action
      products :+ Cart.addToCart(Product.findByName(product).get)  //get product from model
      Redirect(routes.BrowseController.list) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      Ok(views.html.cartpage(products.toList)) //render view template
  }**/
}
