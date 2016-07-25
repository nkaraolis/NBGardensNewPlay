package controllers

import play.api.mvc.{Action, Controller}
import models.{Category, Product}


/**
  * Created by Administrator on 08/07/2016.
  */
class BrowseController extends Controller{

  def productList(cart: String) = Action {
    implicit request =>  //controller action
      val products = Product.findByCart(cart)  //get product from model
      Ok(views.html.ProductList(products)) //render view template
  }

  def categoryList = Action {
    implicit request =>  //controller action
      val categories = Category.findAll  //get product from model
      Ok(views.html.browseCat(categories)) //render view template
  }
}
