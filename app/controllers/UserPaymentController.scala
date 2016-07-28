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
class UserPaymentController @Inject() extends Controller{

  private val paymentForm : Form[CustomerPayment] =
    Form(mapping(
      "Username" -> nonEmptyText,
      "Payment Method" -> nonEmptyText,
      "Name on Card" -> nonEmptyText,
      "Card Number" -> nonEmptyText,
      "Start Date" -> nonEmptyText,
      "Expiry Date" -> nonEmptyText,
      "Security Code" -> nonEmptyText,
      "Issue No" -> text
    )(CustomerPayment.apply)(CustomerPayment.unapply))

  def userPayments = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        paymentForm.bind(request2flash.data)
      else
        paymentForm
      Ok(views.html.userPayments(form))
  }

  def saveChanges = Action {
    implicit request =>
      val editPaymentsForm = paymentForm.bindFromRequest()
      editPaymentsForm.fold(hasErrors = {
        form =>
          Redirect(routes.UserPaymentController.userPayments()).flashing(Flash(form.data) + ("error" -> Messages("Error in payment details")))
      }, success = {
        editPayment =>

          val currentCustomer = Customer.findCustomer(request.session.get("username").get)

          if(CustPayment.findPaymentsByUsername(request.session.get("username").get).isEmpty) {
            //if theres no payments

            val currentPayment = new CustomerPayment("", "", "", "", "", "", "", "")

            currentPayment.username = currentCustomer.username
            currentPayment.paymentMethod = editPaymentsForm.data("Payment Method")


            if (!(editPaymentsForm.data("Name on Card").length == 0)) {
              currentPayment.nameOnCard = editPaymentsForm.data("Name on Card")
            }
            if (!(editPaymentsForm.data("Card Number").length == 0)) {
              currentPayment.cardNumber = editPaymentsForm.data("Card Number")
            }
            if (!(editPaymentsForm.data("Start Date").length == 0)) {
              currentPayment.startDate = editPaymentsForm.data("Start Date")
            }
            if (!(editPaymentsForm.data("Expiry Date").length == 0)) {
              currentPayment.expiryDate = editPaymentsForm.data("Expiry Date")
            }
            if (!(editPaymentsForm.data("Security Code").length == 0)) {
              currentPayment.securityCode = editPaymentsForm.data("Security Code")
            }
            if (!(editPaymentsForm.data("Issue No").length == 0)) {
              currentPayment.issueNo = editPaymentsForm.data("Issue No")
            }

            CustPayment.add(currentPayment)

            Redirect(routes.UserPaymentController.userPayments())

          }else {

            val currentPayments = CustPayment.findPaymentsByUsername(currentCustomer.username)

            if (CustPayment.getPaymentByMethod(currentPayments, editPaymentsForm.data("Payment Method")).isEmpty) {
              //if the exisiting address but isn't the same type submitted.

              val currentPayment = new CustomerPayment("", "", "", "", "", "", "", "")

              currentPayment.username = currentCustomer.username
              currentPayment.paymentMethod = editPaymentsForm.data("Payment Method")

              if (!(editPaymentsForm.data("Name on Card").length == 0)) {
                currentPayment.nameOnCard = editPaymentsForm.data("Name on Card")
              }
              if (!(editPaymentsForm.data("Card Number").length == 0)) {
                currentPayment.cardNumber = editPaymentsForm.data("Card Number")
              }
              if (!(editPaymentsForm.data("Start Date").length == 0)) {
                currentPayment.startDate = editPaymentsForm.data("Start Date")
              }
              if (!(editPaymentsForm.data("Expiry Date").length == 0)) {
                currentPayment.expiryDate = editPaymentsForm.data("Expiry Date")
              }
              if (!(editPaymentsForm.data("Security Code").length == 0)) {
                currentPayment.securityCode = editPaymentsForm.data("Security Code")
              }
              if (!(editPaymentsForm.data("Issue No").length == 0)) {
                currentPayment.issueNo = editPaymentsForm.data("Issue No")
              }

              CustPayment.add(currentPayment)

              Redirect(routes.UserPaymentController.userPayments())

            } else {

              if ((!(editPaymentsForm.data("Payment Method").length == 0)) && ((editPaymentsForm.data("Payment Method") == "Credit") || (editPaymentsForm.data("Payment Method") == "Debit"))) {

                val currentPayment = CustPayment.getPaymentByMethod(currentPayments, editPaymentsForm.data("Payment Method")).get

                if (!(editPaymentsForm.data("Name on Card").length == 0)) {
                  currentPayment.nameOnCard = editPaymentsForm.data("Name on Card")
                }
                if (!(editPaymentsForm.data("Card Number").length == 0)) {
                  currentPayment.cardNumber = editPaymentsForm.data("Card Number")
                }
                if (!(editPaymentsForm.data("Start Date").length == 0)) {
                  currentPayment.startDate = editPaymentsForm.data("Start Date")
                }
                if (!(editPaymentsForm.data("Expiry Date").length == 0)) {
                  currentPayment.expiryDate = editPaymentsForm.data("Expiry Date")
                }
                if (!(editPaymentsForm.data("Security Code").length == 0)) {
                  currentPayment.securityCode = editPaymentsForm.data("Security Code")
                }
                if (!(editPaymentsForm.data("Issue No").length == 0)) {
                  currentPayment.issueNo = editPaymentsForm.data("Issue No")
                }

                val updatePayment = currentPayment
                val paymentSession = request.session +
                  ("Name on Card" -> updatePayment.nameOnCard) +
                  ("Card Number" -> updatePayment.cardNumber) +
                  ("Start Date" -> updatePayment.startDate) +
                  ("Expiry Date" -> updatePayment.expiryDate) +
                  ("Security Code" -> updatePayment.securityCode) +
                  ("Issue No" -> updatePayment.issueNo)
                Redirect(routes.UserPaymentController.userPayments()).withSession(paymentSession)
              }
              else {
                val paymentSession = request.session +
                  ("Name on Card" -> editPaymentsForm.data("Name on Card")) +
                  ("Card Number" -> editPaymentsForm.data("Card Number")) +
                  ("Start Date" -> editPaymentsForm.data("Start Date")) +
                  ("Expiry Date" -> editPaymentsForm.data("Expiry Date")) +
                  ("Security Code" -> editPaymentsForm.data("Security Code")) +
                  ("Issue No" -> editPaymentsForm.data("Issue No"))
                Redirect(routes.UserPaymentController.userPayments()).withSession(paymentSession)
              }
            }
          }
      })
  }





}