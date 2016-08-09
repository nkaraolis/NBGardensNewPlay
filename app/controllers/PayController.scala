package controllers

import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import controllers._
import play.api.i18n.Messages
import scala.concurrent.ExecutionContext.Implicits.global
import controllers.CartController
import models.{CustomerCardDB, PaymentDetails}
import play.api.data.format.Formats._


/**
  * Created by Administrator on 21/07/2016.
  */
class PayController extends Controller {

  //val items = Cart.findAllInCart  //get product from model

   //a payment form for the customer to select a previously saved card and a payment method (Pay Now or Pay Later)
  val PayDetailsForm: Form[PaymentDetails] = Form(
    mapping(
      "Card" -> of[String],
      "Method" -> of[String]
    )
    (PaymentDetails.apply)
    (PaymentDetails.unapply)
  )



  // a form contents information about a card for the user to pay
  val CardForm: Form[CardDetails] = Form(
    mapping(
      "Payment Method" -> nonEmptyText,
      "Name on Card" -> nonEmptyText,
      "Card No" -> nonEmptyText,
      "Start Date" -> nonEmptyText,
      "Expiry Date" -> nonEmptyText,
      "Security Code" -> of[String],
      "Issue No" -> of[String]
    )
    (CardDetails.apply)
    (CardDetails.unapply)
  )





  def options = Action{
    implicit request =>
      val value = CardForm.bindFromRequest.data("Payment Method")
      if(value.equals("Paypal")) {
        val categories = Category.findAll
        Ok(views.html.browseCat(categories))
      }
      else{
        Ok("")
      }
  }



  def readyToPay(items: String, total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      }
      println("inside the readyToPay method: " + items)
      val username = request.session.get("username").get
      //Ok(views.html.payPage(items, total, CustomerCardDB.loadCards(username.toString), PayDetailsForm)) //render view template
      val cardNo = null
      Ok(views.html.payPage(items, total, PayDetailsForm, cardNo)) //render view template

  }

  //This method is to add a customer's payment card to their order
  def addCard(items:String, total:Double, cardNo: String) = Action {
    implicit request =>
      //val findCard = CustomerCardDB.findCard(cardNo)
      Ok(views.html.payPage(items, total, PayDetailsForm, cardNo)) //render view template
      //Redirect(routes.PayController.finishCheckout)
  }




  //This method saves the customer's order to the NBGardensOrders database
  def save (products: String, username: String, total: Double, cardNo: String) = Action {
    implicit request =>
      val payForm = PayDetailsForm.bindFromRequest()
      payForm.fold(success = {
        newOrder =>
       val cardNumber = payForm.get.cardNumber
       val method = payForm.get.method
      //val newCard = CardDetails(newCardForm.get.method, newCardForm.data("Name on Card"), newCardForm.data("Card No"), newCardForm.data("Start Date"), newCardForm.data("Expiry Date"), newCardForm.data("Security Code"), newCardForm.data("Issue No"))
      //val selectedCard = CustomerCardDB.findCard(cardId).get
      val newID = OrderDB.findNextID()
      val status = "Order Made"
      val datetime = OrderDB.getDateTime()
      var order = new OrderDB(newID, username, Cart.productsInCart, total, datetime, status, cardNo, method)
      MongoConnector.collectionOrder.insert(order)
      //CardDetails.add(newCard)
      //Empty Cart
      Cart.clearCart()
          Redirect(routes.BrowseController.categoryList)
      }, hasErrors = {
        form =>
          Redirect(routes.PayController.newCheckout(products, total)).flashing(Flash(form.data))
      })

  }




  def newCheckout(products: String, total:Double) = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        PayDetailsForm.bind(request2flash.data)
      else
        PayDetailsForm
      //val cards = CustomerCardDB.loadCards(request.session.get("username").toString)
      Ok(views.html.payPage(products, total, PayDetailsForm, null))
  }



}
