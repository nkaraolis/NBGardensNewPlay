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

  /*private val addressForm : Form[CustomerAddress] =
    Form(mapping(
      "Username" -> nonEmptyText,
      "Address Type" -> nonEmptyText,
      "Full Name" -> nonEmptyText,
      "Address Line 1" -> nonEmptyText,
      "Address Line 2" -> nonEmptyText,
      "Town/City" -> nonEmptyText,
      "County" -> nonEmptyText,
      "Postcode" -> nonEmptyText
    )(CustomerAddress.apply)(CustomerAddress.unapply))*/

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

  /*
  def saveChanges = Action {
    implicit request =>
      val editAddressForm = addressForm.bindFromRequest()
      editAddressForm.fold(hasErrors = {
        form =>
          Redirect(routes.UserAddressesController.userAddresses()).flashing(Flash(form.data) + ("error" -> Messages("Error in address")))
      }, success = {
        editAddress =>

          val currentCustomer = Customer.findCustomer(request.session.get("username").get)

          if(CustAddress.findAddressesByUsername(request.session.get("username").get).isEmpty) { //if theres no addresses

            val currentAddress = new CustomerAddress("","","","","","","","")

            currentAddress.username = currentCustomer.username
            currentAddress.addressType = editAddressForm.data("Address Type")

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

            CustAddress.add(currentAddress)

            Redirect(routes.UserAddressesController.userAddresses())

          }else{

            val currentAddresses = CustAddress.findAddressesByUsername(currentCustomer.username)

            if(CustAddress.getAddressByType(currentAddresses,editAddressForm.data("Address Type")).isEmpty){ //if the exisiting address but isn't the same type submitted.

              val currentAddress = new CustomerAddress("","","","","","","","")

              currentAddress.username = currentCustomer.username
              currentAddress.addressType = editAddressForm.data("Address Type")

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

              CustAddress.add(currentAddress)

              Redirect(routes.UserAddressesController.userAddresses())

            }else{    //if existing address

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
            }
          }
      })
  }*/
}