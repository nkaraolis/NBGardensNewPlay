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


/**
  * Created by Administrator on 21/07/2016.
  */
class PayController extends Controller {

  //val items = Cart.findAllInCart  //get product from model

  // a form contents information about a card for the user to pay
  val CardForm: Form[cardDetails] = Form(
    mapping(
      "Payment Method" -> nonEmptyText,
      "Name on Card" -> nonEmptyText,
      "Card No" -> nonEmptyText,
      "Start Date" -> nonEmptyText,
      "Expiry Date" -> nonEmptyText,
      "Security Code" -> of[String],
      "Issue No" -> of[String]
    )
    (cardDetails.apply)
    (cardDetails.unapply)
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

  def payByCard(items: String, total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      }
      Ok(views.html.payByCard(items, total, CardForm)) //render view template
  }

  def payByPaypal(items: String, total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      }
      Ok(views.html.payByPaypal(items, total, CardForm)) //render view template
  }



  def readyToPay(items: String, total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      }
      println("inside the readyToPay method: " + items)
      Ok(views.html.payPage(items, total, CardForm)) //render view template

  }



  //This method saves the customer's order to the NBGardensOrders database
  // and also saves the card details to CVS
  def save (products: String, username: String, total: Double) = Action {
    implicit request =>
      val newCardForm = CardForm.bindFromRequest()
      newCardForm.fold(success = {
        newOrder =>
      val newCard = cardDetails(newCardForm.get.method, newCardForm.data("Name on Card"), newCardForm.data("Card No"), newCardForm.data("Start Date"), newCardForm.data("Expiry Date"), newCardForm.data("Security Code"), newCardForm.data("Issue No"))
      val payMethod = newCardForm.get.method
      val newID = OrderDB.findNextID()
      val status = "Order Made"
      val datetime = OrderDB.getDateTime()
      var order = new OrderDB(newID, username, Cart.productsInCart, total, datetime, status, payMethod)
      MongoConnector.collectionOrder.insert(order)
      cardDetails.add(newCard)
      //Empty Cart
      Cart.clearCart()
          Redirect(routes.BrowseController.categoryList)
      }, hasErrors = {
        form =>
         // val cart = Cart.convertToCartItems()
          //val total = Cart.calculateCartTotal(cart)
          Redirect(routes.PayController.newCheckout(products, total)).flashing(Flash(form.data))
      })

  }


  def newCheckout(products: String, total:Double) = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        CardForm.bind(request2flash.data)
      else
        CardForm
      Ok(views.html.payPage(products, total, form))
  }



}
