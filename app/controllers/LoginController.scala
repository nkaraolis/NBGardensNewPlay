package controllers

import javax.inject._
import models._
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
import reactivemongo.bson.BSONDocument
import views.html.helper.form
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * This controller is to enable users to log in
  */
@Singleton
class LoginController @Inject() extends Controller {
  /** Creates the form and verifies the data **/
  val LoginForm = Form(tuple(
    "Username" -> nonEmptyText.verifying("Username not found!", CustomerDB.findByUsername(_).nonEmpty),
    "Password" -> nonEmptyText).verifying("user not registered", f => checkUserCredentials(f._1, f._2)))

  /** Checks the form for errors and if successful logs in and creates session **/
  def save = Action {
    implicit request =>
      val loginFormDB = LoginForm.bindFromRequest()
      loginFormDB.fold(success = {
        newLogin =>
          println("Successful login!")
          val currentCustomer = CustomerDB.findByUsername(newLogin._1).head
          val customerSession = request.session +
            ("customerID" -> currentCustomer.getAs[Int]("customerID").get.toString) +
            ("firstName" -> currentCustomer.getAs[String]("fName").get) +
            ("lastName" -> currentCustomer.getAs[String]("lName").get) +
            ("email" -> currentCustomer.getAs[String]("email").get) +
            ("phone" -> currentCustomer.getAs[String]("phone").get) +
            ("username" -> currentCustomer.getAs[String]("username").get)
          Redirect(routes.HomeController.home()).withSession(customerSession)
      }, hasErrors = {
        form =>
          println("Save method runs here if errors in form")
          Redirect(routes.LoginController.newLogin()).flashing(Flash(form.data) +
            ("error" -> Messages("password.error")))
      })
  }

  def newLogin = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        LoginForm.bind(request2flash.data)
      else
        LoginForm
      Ok(views.html.loginOurs(form))
  }

  def logout = Action {
    implicit request =>
      Redirect(routes.HomeController.home()).withNewSession
  }

  /** Match the username and password for login **/
  def checkUserCredentials(username: String, password: String): Boolean = {
    var status: Boolean = false
    val findQuery = BSONDocument(
      "username" -> username,
      "password" -> password
    )
    val foundUser = MongoConnector.collectionCustomer.find(findQuery).cursor[BSONDocument]().collect[List]()
    foundUser onComplete {
      case Failure(e) => throw e
      case Success(readResult) =>
        if (readResult.nonEmpty) {
          status = true
        } else {
          status = false
        }
    }
    Thread.sleep(500)
    status
  }
}