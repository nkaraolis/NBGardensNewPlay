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
  /**val PayDetailsForm: Form[PaymentDetails] = Form(
    mapping(
      "Card" -> of[String],
      "Method" -> of[String],
      "Items" -> of[String],
      "Total" -> of[Double]
    )
    (PaymentDetails.apply)
    (PaymentDetails.unapply)
  )**/

  val PayDetailsForm = Form(tuple(
    "Method" -> of[String],
    "Card" -> of[String]

  ))


//  val PayDetailsForm = Form(tuple(
//    "Card" -> of[String],
//    "Method" -> of[String],
//    "Items" -> of[String],
//    "Total" -> of[Double]))

//  // a form contents information about a card for the user to pay
//  val CardForm: Form[CardDetails] = Form(
//    mapping(
//      "Payment Method" -> nonEmptyText,
//      "Name on Card" -> nonEmptyText,
//      "Card No" -> nonEmptyText,
//      "Start Date" -> nonEmptyText,
//      "Expiry Date" -> nonEmptyText,
//      "Security Code" -> of[String],
//      "Issue No" -> of[String]
//    )
//    (CardDetails.apply)
//    (CardDetails.unapply)
//  )



//  def options = Action{
//    implicit request =>
//      val value = CardForm.bindFromRequest.data("Payment Method")
//      if(value.equals("Paypal")) {
//        val categories = Category.findAll
//        Ok(views.html.browseCat(categories))
//      }
//      else{
//        Ok("")
//      }
//  }



  def readyToPay(items: String, total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      }
      //val username = request.session.get("username").get
      //Ok(views.html.payPage(items, total, CustomerCardDB.loadCards(username.toString), PayDetailsForm)) //render view template
      Ok(views.html.addcard(total)) //render view template
  }



  //This method saves the customer's order to the NBGardensOrders database
  def save (username: String, total: Double, cardNo: String) = Action {
    implicit request =>
      println("This is inside the save method, username is: " + username)
      val payForm = PayDetailsForm.bindFromRequest()
      payForm.fold(success = {
        PayDetails =>
       //val cardNumber = payForm.get._1
       val method = payForm.get._1
      //val newCard = CardDetails(newCardForm.get.method, newCardForm.data("Name on Card"), newCardForm.data("Card No"), newCardForm.data("Start Date"), newCardForm.data("Expiry Date"), newCardForm.data("Security Code"), newCardForm.data("Issue No"))
      //val selectedCard = CustomerCardDB.findCard(cardId).get
      val newID = OrderDB.findNextID()
      val status = "Order Made"
      val datetime = OrderDB.getDateTime()
      var order = new OrderDB(newID, username, Cart.productsInCart, total, datetime, status, cardNo, payForm.get._1)
      MongoConnector.collectionOrder.insert(order)
      //CardDetails.add(newCard)
      //Empty Cart
      Cart.clearCart()
          Redirect(routes.BrowseController.categoryList())
      }, hasErrors = {
        form =>
          Redirect(routes.PayController.newCheckout(total)).flashing(Flash(form.data))
      })
  }




  def newCheckout(total:Double) = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        PayDetailsForm.bind(request2flash.data)
      else
        PayDetailsForm
      //val cards = CustomerCardDB.loadCards(request.session.get("username").toString)
      Ok(views.html.payPage(total, PayDetailsForm, null))
  }



}
