package controllers

import javax.inject._
import models._
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
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UpdateAccountController @Inject() extends Controller {

  /** Registration form **/
  val updateForm = Form(tuple(
    "First Name" -> text,
    "Last Name" -> text,
    "Email" -> text.verifying("Email already exists!", CustomerDB.findByEmail(_).isEmpty),
    "Telephone" -> text,
    "Username" -> text.verifying("Username already exists!", CustomerDB.findByUsername(_).isEmpty),
    "Password" -> text))

  /** Checks a user is logged in and then loads the form **/
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

  /** Checks the form for errors and then updates customer details if required **/
  def saveChanges = Action {
    implicit request =>
      val editCustomerForm = updateForm.bindFromRequest()
      editCustomerForm.fold(hasErrors = {
        form =>
          Redirect(routes.UpdateAccountController.updateAccount()).flashing(Flash(form.data) + ("error" -> Messages("updateAccount.validation.errors")))
      }, success = {
        editCustomer =>
          val currentUsername = CustomerDB.findByUsername(request.session.get("username").get).head.getAs[String]("username").get
          if (!(editCustomerForm.data("First Name").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "fName", editCustomerForm.data("First Name"))
          }
          if (!(editCustomerForm.data("Last Name").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "lName", editCustomerForm.data("Last Name"))
          }
          if (!(editCustomerForm.data("Email").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "email", editCustomerForm.data("Email"))
          }
          if (!(editCustomerForm.data("Telephone").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "phone", editCustomerForm.data("Telephone"))
          }
          if (!(editCustomerForm.data("Username").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "username", editCustomerForm.data("Username"))
          }
          if (!(editCustomerForm.data("Password").length == 0)) {
            CustomerDB.updateUserField(currentUsername, "password", editCustomerForm.data("Password"))
          }
          val currentCustomer = CustomerDB.findByUsername(currentUsername).head
          val customerSession = request.session +
            ("customerID" -> currentCustomer.getAs[Int]("customerID").get.toString) +
            ("firstName" -> currentCustomer.getAs[String]("fName").get) +
            ("lastName" -> currentCustomer.getAs[String]("lName").get) +
            ("email" -> currentCustomer.getAs[String]("email").get) +
            ("phone" -> currentCustomer.getAs[String]("phone").get) +
            ("username" -> currentCustomer.getAs[String]("username").get)
          Redirect(routes.UserAccountController.userAccount()).withSession(customerSession)
      })
  }
}