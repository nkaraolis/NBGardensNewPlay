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

  /** New Address form **/
  val newAddressForm = Form(tuple(
    "Full Name" -> nonEmptyText,
    "Address Type" -> nonEmptyText,
    "Line 1" -> nonEmptyText,
    "Line 1" -> nonEmptyText,
    "Town/City" -> nonEmptyText,
    "County" -> nonEmptyText,
    "Postcode" -> nonEmptyText))

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
      val addAddress = newAddressForm.bindFromRequest()
      addAddress.fold(success = {
        newAddress =>
          val currentUser = CustomerDB.findCustomer(request.session.get("username").get)
          val newCustomerAddress = new CustomerAddressDB(currentUser.addresses.size, newAddress._1, newAddress._2, newAddress._3,newAddress._4,newAddress._5,newAddress._6, newAddress._7)
          // Adds a new address to the current user
          CustomerAddressDB.updateAddress(currentUser.username, newCustomerAddress, "$addToSet")
          Redirect(routes.UserAccountController.userAccount())
      }, hasErrors = {
        form =>
          Redirect(routes.UserAddressesController.userAddresses()).flashing(Flash(form.data) + ("error" -> Messages("Error in address")))
      })
  }

  /** Runs the method to delete an address from the database **/
  def deleteAddress(username: String, addressID: Int) = Action {
    implicit request =>
      val currentUser = CustomerDB.findCustomer(username)
      val findAddress = CustomerAddressDB.findAddress(currentUser, addressID)
      CustomerAddressDB.removeAddress(username, findAddress)

      Redirect(routes.UserAccountController.userAccount())
  }
}