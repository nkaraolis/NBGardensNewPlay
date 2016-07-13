package controllers

import javax.inject._
import models.CustomerLogin
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


/**
  * This controller is to able users to log in
  */
@Singleton
class LoginController @Inject() extends Controller {


  def login3(Email:String) = Action {
    implicit request =>
      CustomerLogin.findCustomer(Email).map {
        user =>
          Ok(views.html.confirmation(user.Email))
      }   .getOrElse(NotFound)

  }


    def checkUserCredentials(Email:String, password:String): Boolean = {
      val user = CustomerLogin.findCustomer(Email).get
      var status:Boolean = false
      if(user.password == password) {
        //log the user in
        status = true
      }  else {
        status = false

      }
      status
    }

  def index = Action {
    implicit request =>
      Ok(views.html.loginOurs(LoginForm))
  }


  private val LoginForm: Form[CustomerLogin] = Form(mapping(
    "Email" -> nonEmptyText.verifying("validation.email.nonexistant",
      !CustomerLogin.findCustomer(_).isEmpty),
    "Password" -> nonEmptyText)(CustomerLogin.apply)(CustomerLogin.unapply)
    verifying ("user not registered", f => checkUserCredentials(f.Email, f.password))
  )




  def save = Action {
    implicit  request =>
      val newLoginForm = LoginForm.bindFromRequest()
      newLoginForm.fold(hasErrors = {
        form =>
          Redirect(routes.LoginController.newLogin()).flashing(Flash(form.data) +
            ("error" -> Messages("password.error")))
      }, success = {
        newLogin =>
          Redirect(routes.LoginController.login3(newLogin.Email)).flashing("success" ->
            Messages("customers.new.success", newLogin.Email))
    }
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