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

  val updateForm: Form[CustomerDetails] =
    Form(mapping(
      "First Name" -> text,
      "Last Name" -> text,
      "Email" -> text.verifying("validation.email.duplicate", Customer.findByEmail(_).isEmpty),
      "Telephone" -> text,
      "Username" -> text.verifying("validation.username.duplicate", Customer.findByUsername(_).isEmpty),
      "Password" -> text
    )(CustomerDetails.apply)(CustomerDetails.unapply))


  def updateAccount = Action {
    implicit request =>
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      } else {
        val form = if (request2flash.get("error").isDefined)
          updateForm.bind(request2flash.data)
        else
          updateForm
        Ok(views.html.updateAccount(form))
      }
  }

  def saveChanges = Action {
    implicit request =>
      val editCustomerForm = updateForm.bindFromRequest()
      editCustomerForm.fold(hasErrors = {
        form =>
          Redirect(routes.UpdateAccountController.updateAccount()).flashing(Flash(form.data) + ("error") -> Messages("updateAccount.validation.errors"))
      }, success = {
        editCustomer =>
          val currentCustomer = Customer.findCustomer(request.session.get("username").get)
          if(!(editCustomerForm.data("First Name").length == 0)){
            currentCustomer.firstName = editCustomerForm.data("First Name")
          }
          if(!(editCustomerForm.data("Last Name").length == 0)){
            currentCustomer.lastName = editCustomerForm.data("Last Name")
          }
          if(!(editCustomerForm.data("Email").length == 0)){
            currentCustomer.email = editCustomerForm.data("Email")
          }
          if(!(editCustomerForm.data("Telephone").length == 0)){
            currentCustomer.telephone = editCustomerForm.data("Telephone")
          }
          if(!(editCustomerForm.data("Username").length == 0)){
            currentCustomer.username = editCustomerForm.data("Username")
          }
          if(!(editCustomerForm.data("Password").length == 0)){
            currentCustomer.password = editCustomerForm.data("Password")
          }
          val updateCustomer = Customer.findCustomer(currentCustomer.username)
          val customerSession = request.session +
            ("firstName" -> updateCustomer.firstName) +
            ("lastName" -> updateCustomer.lastName) +
            ("email" -> updateCustomer.email) +
            ("telephone" -> updateCustomer.telephone.toString) +
            ("username" -> updateCustomer.username) +
            ("password" -> updateCustomer.password)
          Redirect(routes.UserAccountController.userAccount()).withSession(customerSession)
      })
  }
}