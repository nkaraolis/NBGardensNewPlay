package controllers

import javax.inject._

import models.{Customer, CustomerDetails}
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

  def registration = Action {
    Ok(views.html.registration(userForm))
  }

  def goHome = Action {
    Redirect(routes.HomeController.home())
  }

  private val userForm : Form[CustomerDetails] =
    Form(mapping(
      "name" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> nonEmptyText,
      "telephone" -> number,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CustomerDetails.apply)(CustomerDetails.unapply))

  def saveCustomer = Action {
    implicit request =>
      val newCustomerForm = userForm.bindFromRequest()
      newCustomerForm.fold(hasErrors = {
        form =>
          Redirect(routes.RegistrationController.registration()).flashing(Flash(form.data) + ("error" -> Messages("register.validation.errors")))
      }, success = {
          newCustomer =>
            Customer.add(newCustomer)
            Redirect(routes.HomeController.home()).flashing("success" -> Messages("customers.new.success", newCustomer.firstName))}
      )
  }

  def newCustomer = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
          userForm.bind(request2flash.data)
      else
          userForm
      Ok(views.html.home())
  }

}
