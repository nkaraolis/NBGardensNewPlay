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
import views.html.helper.form
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UserAddressesController @Inject() extends Controller {

  val newAddressForm: Form[CustomerAddressDB] =
    Form(mapping(
      "Full Name" -> nonEmptyText,
      "Address Type" -> nonEmptyText,
      "Line 1" -> nonEmptyText,
      "Line 2" -> nonEmptyText,
      "Town/City" -> nonEmptyText,
      "County" -> nonEmptyText,
      "Postcode" -> nonEmptyText
    )(CustomerAddressDB.apply)(CustomerAddressDB.unapply))


  def userAddresses = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        newAddressForm.bind(request2flash.data)
      else
        newAddressForm
      Ok(views.html.userAddresses(form))
  }


  /** Checks form for errors and then adds new address to user if correct **/
  def updateAddress = Action {
    implicit request =>
      val updateAddressForm = newAddressForm.bindFromRequest()
      updateAddressForm.fold(success = {
        newAddress =>
          val currentUsername = request.session.get("username").get
          println("Adding a new address")
          // Adds a new address to the current user
          CustomerAddressDB.updateAddress(currentUsername, newAddress, "$addToSet")
          Redirect(routes.UserAccountController.userAccount())
      }, hasErrors = {
        form =>
          Redirect(routes.UserAddressesController.userAddresses()).flashing(Flash(form.data) + ("error" -> Messages("Error in address")))
      })
  }


}