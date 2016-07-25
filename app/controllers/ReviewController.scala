package controllers

import javax.inject._

import models.{Customer, CustomerDetails, CustomerLogin}
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

@Singleton
class ReviewController @Inject() extends Controller {


  def sessionReviewCheck = Action {
    implicit request =>
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      } else {
        null
      }

  }
}