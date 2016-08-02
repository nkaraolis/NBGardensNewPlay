package controllers

import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import controllers._


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
  //This method is called from payPage.scala.html
  def save (products: String, username: String) = Action {
    implicit request =>
      val newCardForm = CardForm.bindFromRequest()
      val newCard = cardDetails(newCardForm.get.method, newCardForm.data("Name on Card"), newCardForm.data("Card No"), newCardForm.data("Start Date"), newCardForm.data("Expiry Date"), newCardForm.data("Security Code"), newCardForm.data("Issue No"))
      //saveOrder(products)
      //Product.saveProductsForAnOrder(products)
      val payMethod = newCardForm.get.method
      saveToDB(username, payMethod)
      cardDetails.add(newCard)
      Redirect(routes.BrowseController.categoryList)
  }



  //save order to the MongoDB
  def saveToDB(username: String, payMethod: String){
    val cart = Cart.productsInCart
    //OrderDB.createNewOrder(1, username, cart, payMethod)
    val status = "Order Made"

    val total:Double = Cart.calculateCartTotal(cart)
    //needs to be sorted before we generate new ID
    val sortedOrders = OrderDB.orders.toList.sortBy(_.ordId)
    //Creates instance of a new order
    var order = OrderDB(sortedOrders.last.ordId + 1, username, cart, total, OrderDB.getDateTime(), status, payMethod)
    //adds to set of orders is OrderDB
    OrderDB.orders += order
    var doc = OrderDB.createDoc(order)
    //adds order to MongoDB
    OrderDB.insertDoc(MongoConnector.collectionOrder, doc)
  }



  def saveOrder (products: String) {
    writeOrders.add(products)
  }
}
