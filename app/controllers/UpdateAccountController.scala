package controllers

import javax.inject._

import models.{Customer, CustomerDetails, CustomerLogin}
import play.api._
import play.api.data.Forms._
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}
import play.api.Play.current
import play.api.data.Form

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UpdateAccountController @Inject() extends Controller {

  def index = Action {
    request => request.session.get("connected").map {
      user => Ok("Hello " + user)
    }.getOrElse {
      Unauthorized(" hhggh")
    }
  }

  val updateForm: Form[CustomerDetails] =
    Form(mapping(
      "First Name" -> nonEmptyText,
      "Last Name" -> nonEmptyText,
      "Email" -> nonEmptyText.verifying("validation.email.duplicate", Customer.findByEmail(_).isEmpty),
      "Telephone" -> number,
      "Username" -> nonEmptyText.verifying("validation.username.duplicate", Customer.findByUsername(_).isEmpty),
      "Passsword" -> nonEmptyText
    )(CustomerDetails.apply)(CustomerDetails.unapply))


  def updateAccount = Action {
    implicit request =>
      Ok(views.html.updateAccount(updateForm))
  }

  def saveChanges = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        updateForm.bind(request2flash.data)
      else
        updateForm
      Ok(views.html.updateAccount(form))
  }
}