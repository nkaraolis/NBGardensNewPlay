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
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class UserPaymentController @Inject() extends Controller{

  /** New Address form **/
  val newCardForm = Form(tuple(
    "Card Type" -> nonEmptyText,
    "Card Number" -> nonEmptyText,
    "Expiry" -> nonEmptyText,
    "Name on Card" -> nonEmptyText))

  /** Loads the card details form **/
  def userPayments = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        newCardForm.bind(request2flash.data)
      else
        newCardForm
      Ok(views.html.userPayments(form))
  }

  /** Checks form for errors and then adds new card details to user if correct **/
  def updatePayment = Action {
    implicit request =>
      val addCard = newCardForm.bindFromRequest()
      addCard.fold(success = {
        newPayment =>
          val currentUser = CustomerDB.findCustomer(request.session.get("username").get)
          val newCustomerCard = new CustomerCardDB(currentUser.cardDetails.size, newPayment._1, newPayment._2, newPayment._3,newPayment._4)
          // Adds a new card to the current user
          CustomerCardDB.updatePayment(currentUser.username, newCustomerCard, "$addToSet")
          Redirect(routes.UserAccountController.userAccount())
      }, hasErrors = {
        form =>
          Redirect(routes.UserAddressesController.userAddresses()).flashing(Flash(form.data) + ("error" -> Messages("Error in address")))
      })
  }

  /** Runs the method to delete a card from the database **/
  def deleteCardDetails(username: String, cardID: Int) = Action {
    implicit request =>
      val currentUser = CustomerDB.findCustomer(username)
      val findAddress = CustomerCardDB.findCardDetails(currentUser, cardID)
      CustomerCardDB.removeCardDetails(username, findAddress)

      Redirect(routes.UserAccountController.userAccount())
  }
}