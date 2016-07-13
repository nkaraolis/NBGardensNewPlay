package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UpdateAccountController  @Inject() extends Controller{


  def index = Action {
    request => request.session.get("connected").map {
      user => Ok("Hello " + user)}.getOrElse{
      Unauthorized(" hhggh")
    }
    }



  def updateAccount = Action {
    implicit request =>
      Ok(views.html.updateAccount())
  }
}