package controllers

import javax.inject._
<<<<<<< HEAD
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
=======

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

    def login(Email:String, password:String): Boolean = {
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

//  def show(Email: String) = Action {
//    implicit request =>
//      CustomerLogin.findCustomer(Email).map {
//        customer =>
//          Ok(views.html.details(CustomerLogin(Email, "password")))
//      }.getOrElse(NotFound)
//  }


  private val LoginForm: Form[CustomerLogin] = Form(mapping(
    "Email" -> nonEmptyText,
    "password" -> nonEmptyText)(CustomerLogin.apply)(CustomerLogin.unapply))


  def save = Action {
    implicit  request =>
      val newLoginForm = LoginForm.bindFromRequest()
      newLoginForm.fold(hasErrors = {

        form =>
          Redirect(routes.LoginController.index()).flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))

      }, success = {
        newLogin =>
//          Redirect(routes.HomeController.confirm(newLogin.Email)).flashing("success" ->
//            Messages("customers.new.success", newLogin.Email))
          Redirect(routes.HomeController.home()).flashing("success" -> Messages("customers.new.success", newLogin.Email))}
//          val status2:Boolean = login(newLogin.Email, newLogin.password)
//
//          if (status2) {
//            Redirect(routes.HomeController.confirm(newLogin.Email)).flashing("success" ->
//              Messages("customers.new.success", newLogin.Email))
//          } else {
//            Redirect(routes.LoginController.index())
//            //Redirect(routes.LoginController.index()).flashing(Flash(form.data) + ("error" -> Messages("login.errors")))
//          }

      )
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