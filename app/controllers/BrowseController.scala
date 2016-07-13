package controllers

import play.api.mvc.{Action, Controller}
import models.Product

/**
  * Created by Administrator on 08/07/2016.
  */
class BrowseController extends Controller{

  def list = Action {    implicit request =>  //controller action
    val products = Product.findAll  //get product from model
    Ok(views.html.ProductList(products)) //render view template

  }

}
