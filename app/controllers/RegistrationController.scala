package controllers

import javax.inject._
import models.{Customer, CustomerDetails, CustomerLogin}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}
import play.api.Play.current

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class RegistrationController  @Inject() extends Controller{

  private val userForm : Form[CustomerDetails] =
    Form(mapping(
      "First Name" -> nonEmptyText,
      "Last Name" -> nonEmptyText,
      "Email" -> nonEmptyText.verifying("validation.email.duplicate", Customer.findByEmail(_).isEmpty),
      "Telephone" -> nonEmptyText,
      "Username" -> nonEmptyText.verifying("validation.username.duplicate", Customer.findByUsername(_).isEmpty),
      "Password" -> nonEmptyText
    )(CustomerDetails.apply)(CustomerDetails.unapply))

  def saveCustomer = Action {
    implicit request =>
      val newCustomerForm = userForm.bindFromRequest()
      newCustomerForm.fold(hasErrors = {
        form =>
          Redirect(routes.RegistrationController.newCustomer()).flashing(Flash(form.data) + ("error" -> Messages("register.validation.errors")))
      }, success = {
          newCustomer =>
            Customer.add(newCustomer)
            Redirect(routes.LoginController.newLogin()).flashing("success" -> Messages("customers.new.success", newCustomer.firstName))}
      )
  }


  def newCustomer = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
          userForm.bind(request2flash.data)
      else
          userForm
       Ok(views.html.registration(form))
  }

def show = Action {
  implicit request => val customers = Customer.findAllCustomer
    Ok(views.html.customerall(customers))
}

}
