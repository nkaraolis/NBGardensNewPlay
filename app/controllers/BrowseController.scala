package controllers
import javax.inject._

import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import models.Product
import play.twirl.api.Html

/**
  * Created by Administrator on 08/07/2016.
  */
class BrowseController extends Controller{

  def list = Action {    implicit request =>  //controller action
    val products = Product.findAll  //get product from model
    Ok(views.html.ProductList(products)) //render view template

  }
  def index = Action{
    Ok(views.html.products())
  }
//Changes
}
