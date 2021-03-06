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
class AboutController  @Inject() extends Controller{

  def about = Action {
    implicit request =>
    Ok(views.html.about())
  }

}