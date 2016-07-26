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

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UserAddressesController  @Inject() extends Controller{

  private val addressForm : Form[CustomerAddress] =
    Form(mapping(
      "Username" -> nonEmptyText,
      "Address Type" -> nonEmptyText,
      "Full Name" -> nonEmptyText,
      "Address Line 1" -> nonEmptyText,
      "Address Line 2" -> nonEmptyText,
      "Town/City" -> nonEmptyText,
      "County" -> nonEmptyText,
      "Postcode" -> nonEmptyText
    )(CustomerAddress.apply)(CustomerAddress.unapply))

  def userAddresses = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        addressForm.bind(request2flash.data)
      else
        addressForm
      Ok(views.html.userAddresses(form))
  }

  def saveChanges = Action {
    implicit request =>
      val editAddressForm = addressForm.bindFromRequest()
      editAddressForm.fold(hasErrors = {
        form =>
          Redirect(routes.UserAddressesController.userAddresses()).flashing(Flash(form.data) + ("error" -> Messages("Error in address")))
      }, success = {
        editAddress =>

          val currentCustomer = Customer.findCustomer(request.session.get("username").get)

          val currentAddresses = CustAddress.findAddressesByUsername(currentCustomer.username)

          if ((!(editAddressForm.data("Address Type").length == 0)) && ((editAddressForm.data("Address Type") == "Shipping") || (editAddressForm.data("Address Type") == "Billing"))) {

            val currentAddress = CustAddress.getAddressByType(currentAddresses, editAddressForm.data("Address Type")).get

            if (!(editAddressForm.data("Full Name").length == 0)) {
              currentAddress.fullName = editAddressForm.data("Full Name")
            }
            if (!(editAddressForm.data("Address Line 1").length == 0)) {
              currentAddress.addressLine1 = editAddressForm.data("Address Line 1")
            }
            if (!(editAddressForm.data("Address Line 2").length == 0)) {
              currentAddress.addressLine2 = editAddressForm.data("Address Line 2")
            }
            if (!(editAddressForm.data("Town/City").length == 0)) {
              currentAddress.townCity = editAddressForm.data("Town/City")
            }
            if (!(editAddressForm.data("County").length == 0)) {
              currentAddress.county = editAddressForm.data("County")
            }
            if (!(editAddressForm.data("Postcode").length == 0)) {
              currentAddress.postCode = editAddressForm.data("Postcode")
            }

            val updateAddress = currentAddress
            val addressSession = request.session +
              ("fullName" -> updateAddress.fullName) +
              ("addressLine1" -> updateAddress.addressLine1) +
              ("addressLine2" -> updateAddress.addressLine2) +
              ("townCity" -> updateAddress.townCity) +
              ("county" -> updateAddress.county) +
              ("postcode" -> updateAddress.postCode)
            Redirect(routes.UserAddressesController.userAddresses()).withSession(addressSession)
          }
          else{
            val addressSession = request.session +
              ("fullName" -> editAddressForm.data("Full Name")) +
              ("addressLine1" -> editAddressForm.data("Address Line 1")) +
              ("addressLine2" -> editAddressForm.data("Address Line 2")) +
              ("townCity" -> editAddressForm.data("Town/City")) +
              ("county" -> editAddressForm.data("County")) +
              ("postcode" -> editAddressForm.data("Postcode"))
            Redirect(routes.UserAddressesController.userAddresses()).withSession(addressSession)
          }
      })
  }





}