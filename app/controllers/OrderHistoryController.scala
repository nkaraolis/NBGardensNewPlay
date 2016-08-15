package controllers

import javax.inject.{Inject, Singleton}

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
import models.OrderDB
/**
  * Created by Administrator on 25/07/2016.
  */
@Singleton
class OrderHistoryController @Inject() extends Controller {

  //point of entry to History page, this gets the logged in customer's orders
  def newPage(cid:String) = Action {
    implicit request =>
      Ok(views.html.history(OrderDB.getOrdersByCusId(cid)))

  }



  //method to display an order's details
  def track(id:Int) = Action {
    implicit request =>
      OrderDB.findOrderById(id).map { order =>
        Ok(views.html.trackOrder(order))
      }.getOrElse(NotFound)

  }






}


