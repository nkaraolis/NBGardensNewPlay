package controllers

/**
  * Created by Administrator on 07/07/2016.
  */
import javax.inject._

import models.CustomerLogin
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    implicit request =>
    Ok(views.html.home())
  }

//  def confirm(email: String) = Action {
//    implicit request =>
//      Ok(views.html.confirmation(email))
//  }

//  def confirm(user: CustomerLogin) = Action {
//    implicit request =>
//      Ok(views.html.confirmation(user))
//  }

  def home() = Action {
    implicit request =>
      Ok(views.html.home())
  }
}