package controllers

import play.api.mvc._
import models.{Cart, cardDetails, writeOrders}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.{Category, Product}

/**
  * Created by Administrator on 21/07/2016.
  */
class PayController extends Controller {

  val items = Cart.findAllInCart  //get product from model

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
      Ok(views.html.payPage(items, total, CardForm)) //render view template
  }

  // save the card details to CVS
  def save (products: String) = Action {
    implicit request =>
      val newCardForm = CardForm.bindFromRequest()
      val newCard = cardDetails(newCardForm.get.method, newCardForm.data("Name on Card"), newCardForm.data("Card No"), newCardForm.data("Start Date"), newCardForm.data("Expiry Date"), newCardForm.data("Security Code"), newCardForm.data("Issue No"))
      saveOrder(products)
      cardDetails.add(newCard)
      Redirect(routes.BrowseController.categoryList)
  }

  def saveOrder (products: String) {
    writeOrders.add(products)
  }
}
