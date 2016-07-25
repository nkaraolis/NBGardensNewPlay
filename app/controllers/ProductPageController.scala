package controllers

import models.{Product}
import play.api.mvc.{Action, Controller}

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
