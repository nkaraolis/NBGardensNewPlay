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



//    def login(Email:String, password:String): Boolean = {
//      val user = CustomerLogin.findCustomer(Email).get
//      var status:Boolean = false
//      if(user.password == password) {
//        status = true
//      }  else {
//        status = false
//      }
//      status
//    }
//
//  def login2(Email:String, password:String): Action = {
//    implicit val aUser = CustomerLogin.findCustomer(Email).get
//    aUser match {
//      case false ⇒ Redirect(routes.LoginController.index()).flashing("error"
//        -> Messages("validation.errors"))
//      case true  ⇒ Redirect(routes.HomeController.confirm(aUser.Email))
//        .flashing("success" ->
//        Messages("login.success", aUser.Email))
//    }
//  }

  def login3(Email:String) = Action {
    implicit request =>
      CustomerLogin.findCustomer(Email).map {
        user =>
          Ok(views.html.confirmation(user))
      }   .getOrElse(NotFound)
  }


  def index = Action {
    implicit request =>
      Ok(views.html.loginOurs(LoginForm))
  }

//  def show(Email: String) = Action {
//    implicit request =>
//      CustomerLogin.findCustomer(Email).map {
//        customer =>
//          Ok(views.html.details(CustomerLogin(Email, "password")))
//      }.getOrElse(NotFound)
//  }


  private val LoginForm: Form[CustomerLogin] = Form(mapping(
    "email" -> nonEmptyText.verifying("validation.email.nonexistant", !CustomerLogin.findCustomer(_).isEmpty),
    "password" -> nonEmptyText)(CustomerLogin.apply)(CustomerLogin.unapply))


  def save = Action {
    implicit  request =>
      val newLoginForm = LoginForm.bindFromRequest()
      newLoginForm.fold(hasErrors = {
        form =>
          Redirect(routes.LoginController.newLogin()).flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
      }, success = {
        newLogin =>
          Redirect(routes.LoginController.login3(newLogin.email)).flashing("success" ->
            Messages("login.form.success", newLogin.email))

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