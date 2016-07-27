package controllers

import javax.inject.Inject

import models._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
import play.api.Play.current
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages.Implicits._
import views.html.helper.form
import models.{Order}
/**
  * Created by Administrator on 25/07/2016.
  */
class OrderHistoryController @Inject() extends Controller {

  //point of entry to History page
  def newPage(cid:String) = Action {
    implicit request =>
      Ok(views.html.history(Order.getOrdersByCusId(cid)))

  }



  //point of entry to History page
  def newPage2(cid:String) = Action {
    implicit request =>
      Ok(views.html.history(Order.getOrdersByCusId(cid)))
  }




  //method to display an order's details
  def track(id:Int) = Action {
    implicit request =>
      Order.findOrderById(id).map { order =>
        Ok(views.html.trackOrder(order))
      }.getOrElse(NotFound)

  }






}


