package controllers

import javax.inject._
import models.{CustomerCardDB, _}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class RegistrationController @Inject() extends Controller {

  /** Registration form **/
  val newUserForm = Form(tuple(
    "First Name" -> nonEmptyText,
    "Last Name" -> nonEmptyText,
    "Email" -> nonEmptyText.verifying("Email already exists!", CustomerDB.findByEmail(_).isEmpty),
    "Telephone" -> nonEmptyText,
    "Username" -> nonEmptyText.verifying("Username already exists!", CustomerDB.findByUsername(_).isEmpty),
    "Password" -> nonEmptyText))


  /** Save the new customer into the database **/
  def saveCustomer = Action {
    implicit request =>
      val newCustomerForm = newUserForm.bindFromRequest()
      newCustomerForm.fold(success = {
        newCustomer =>
          //Finds the next customer ID from the database
          val newID = CustomerDB.findNextID()
          val newerCustomer = new CustomerDB(newID, newCustomer._1, newCustomer._2, newCustomer._3, newCustomer._4, newCustomer._5, newCustomer._6, List[CustomerAddressDB](), List[CustomerCardDB]())
          MongoConnector.collectionCustomer.insert(newerCustomer)
          Redirect(routes.LoginController.newLogin())
      }, hasErrors = {
        form =>
          println("runs if errors in registration form")
          Redirect(routes.RegistrationController.newCustomer()).flashing(Flash(form.data) + ("error" -> Messages("register.validation.errors")))
      })
  }


  /** Loads up the registration page with errors if any **/
  def newCustomer = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        newUserForm.bind(request2flash.data)
      else
        newUserForm
      Ok(views.html.registration(form))
  }


//  def show = Action {
//    implicit request => val customers = Customer.findAllCustomer
//      Ok(views.html.customerall(customers))
//  }

}