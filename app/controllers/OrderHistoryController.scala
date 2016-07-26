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
/**
  * Created by Administrator on 25/07/2016.
  */
class OrderHistoryController @Inject() extends Controller {

  //point of entry to History page
  def newPage = Action {
    implicit request =>
      Ok(views.html.history(writeOrders.findOrders))
  }
}
