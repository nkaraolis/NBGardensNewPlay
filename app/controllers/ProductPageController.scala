package controllers

import play.api.mvc.{Action, Controller}
import models.Product

/**
  * Created by Administrator on 08/07/2016.
  */

class ProductPageController extends Controller {
  def goToProduct(product: String) = Action {
    implicit request =>  //controller action
      val clickedProduct = Product.findProductByName(product).get
      Ok(views.html.productPage(clickedProduct))
  }
}
