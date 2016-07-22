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
class UserAccountController  @Inject() extends Controller{

  var currentCustomer = new CustomerDetails("", "", "", "", "", "")

  val updateForm: Form[CustomerDetails] =
    Form(mapping(
      "First Name" -> text,
      "Last Name" -> text,
      "Email" -> text.verifying("validation.email.duplicate", Customer.findByEmail(_).isEmpty),
      "Telephone" -> text,
      "Username" -> default(text, currentCustomer.username),
      "Password" -> text
    )(CustomerDetails.apply)(CustomerDetails.unapply))

  def userAccount = Action {
    implicit request =>
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      } else {
        currentCustomer = Customer.findCustomer(request.session.get("username").get)
        val formMapping = Map("firstName" -> currentCustomer.firstName,
          "lastName" -> currentCustomer.lastName,
          "email" -> currentCustomer.email,
          "telephone" -> currentCustomer.telephone,
          "password" -> currentCustomer.password
        )
        val userData = updateForm.bind(formMapping)
        Ok(views.html.userAccount(userData))
      }
  }
}