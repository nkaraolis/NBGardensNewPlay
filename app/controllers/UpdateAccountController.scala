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
import play.api.mvc.Session

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UpdateAccountController @Inject() extends Controller {

  var currentCustomer = new CustomerDetails("", "", "", "", "", "")

  def index = Action {
    request => request.session.get("connected").map {
      user => Ok("Hello " + user)
    }.getOrElse {
      Unauthorized(" hhggh")
    }
  }


  currentCustomer.username = "nameYo"

  val updateForm: Form[CustomerDetails] =
    Form(mapping(
      "First Name" -> text,
      "Last Name" -> text,
      "Email" -> text.verifying("validation.email.duplicate", Customer.findByEmail(_).isEmpty),
      "Telephone" -> text,
      "Username" -> default(text, currentCustomer.username),
      "Passsword" -> text
    )(CustomerDetails.apply)(CustomerDetails.unapply))


  def updateAccount = Action {
    implicit request =>
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      } else {
        //TODO Update customerDetails to use telephone as a string
        currentCustomer = new CustomerDetails(request.session.get("firstName").toString,request.session.get("lastName").toString,request.session.get("email").toString,request.session.get("telephone").toString,request.session.get("username").toString,request.session.get("passsword").toString)
        Ok(views.html.updateAccount(updateForm))
      }
  }

  def saveChanges = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        updateForm.bind(request2flash.data)
      else
        updateForm
     /* if(!(updateForm.value.head.firstName.length == 0)) {

        Customer.findByUsername(currentCustomer.username).head.firstName = updateForm.data("First Name")
      }*/
      Ok(views.html.updateAccount(form))
  }
}