package controllers

import play.api.mvc._
import models.{Cart, Product, Review}
import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._


class ProductPageController extends Controller {

  /**val NewForm = routes.ReviewController.reviewForm
  private val reviewForm : Form[Review] =
    Form(mapping(
      "Customer Name" -> text,
      "Product ID" -> text,
      "Review" -> nonEmptyText

    )(Review.apply)(Review.unapply))**/

  def goToProduct(product: String) = Action {
    implicit request =>  //controller action

      var clickedProduct = Product.findProductByName(product).get

      Ok(views.html.productPage(clickedProduct))

  }


  /*def ReviewData(review: String) {
    val reviewForm = Form(
      mapping(
        "review" -> nonEmptyText.verifying("Review can not be left empty")
      )(ReviewData.apply)(ReviewData.unapply)
    )
  }*/



  /**var products: Array[Product] = Array.empty
    **
    *def add(product: String, Qty: String) = Action {
    *implicit request =>  //controller action
    *products :+ Cart.addToCart(Product.findByName(product).get)  //get product from model
    *Redirect(routes.BrowseController.list) //render view template
    * }
    **
    *def remove(product: String) = Action {
    *implicit request =>  //controller action
    *products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
    *Ok(views.html.cartpage(products.toList)) //render view template
    *}**/
}
