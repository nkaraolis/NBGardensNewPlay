package controllers

import javax.inject._

import models.Customer
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.mvc.{Action, Controller, Flash}
import play.api.Play.current
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages.Implicits._
import views.html.helper.form

/**
  * This controller is to able users to log in
  */
@Singleton
class LoginController @Inject() extends Controller {



  //  def login(Email:String, password:String) {
  //    val user = findCustomer(Email).get
  //    print(user)
  //    if(user.password == password) {
  //      //log the user in
  //      print("@username is logged in")
  //    }  /*else {
  //      print("The password you have entered is incorrect")
  //    }*/
  //  }


  def index = Action {
    implicit request =>
      Ok(views.html.loginOurs(LoginForm))
  }



  def show(Email: String) = Action {
    implicit request =>
      Customer.findCustomer(Email).map {
        customer =>
          Ok(views.html.details(Customer(Email, "password")))
      }.getOrElse(NotFound)
  }

  private val LoginForm: Form[Customer] = Form(mapping("Email" ->
    nonEmptyText, "Password" ->
    nonEmptyText)(Customer.apply)(Customer.unapply))


  def save = Action {
    implicit  request =>
      val newLoginForm = LoginForm.bindFromRequest()
      newLoginForm.fold(hasErrors = {
        form =>
          Redirect(routes.LoginController.newLogin()).flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
      }, success = {
        newLogin =>
          Redirect(routes.LoginController.show(newLogin.Email)).flashing("success" ->
            Messages("customers.new.success", newLogin.Email))
      })
  }


  def newLogin = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        LoginForm.bind(request2flash.data)
      else
        LoginForm
      Ok(views.html.loginOurs(form))
  }




}