package controllers

import play.api.mvc.{Action, Controller}
import models.{Cart, Product, aFormForCart}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
  * Created by Administrator on 08/07/2016.
  */
class BrowseController extends Controller{

  def list = Action {
    implicit request =>  //controller action
      val products = Product.findAll  //get product from model

      Ok(views.html.ProductList(products, CartForm)) //render view template
  }
  val CartForm: Form[aFormForCart] = Form(mapping(
    "Qty" -> of[String])(aFormForCart.apply)(aFormForCart.unapply))
}
