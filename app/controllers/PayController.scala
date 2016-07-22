package controllers

import play.api.mvc._
import models.{Cart, cardDetails}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
  * Created by Administrator on 21/07/2016.
  */
class PayController extends Controller {

  val items = Cart.findAllInCart  //get product from model

  val CardForm: Form[cardDetails] = Form(
    mapping(
      "method" -> nonEmptyText,
      "name" -> nonEmptyText,
      "cardNu" -> nonEmptyText,
      "exp" -> nonEmptyText,
      "securityCode" -> nonEmptyText,
      "issueNu" -> of[String],
      "start" -> of[String]
    )
    (cardDetails.apply)
    (cardDetails.unapply)
  )

  def readyToPay(total:Double) = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      }
      Ok(views.html.payPage(items, total, CardForm)) //render view template
  }

  def save (products: String) = Action {
    implicit request =>

      val newCardForm = CardForm.bindFromRequest()
      val newCard = cardDetails(newCardForm.get.method, newCardForm.get.name, newCardForm.get.cardNu, newCardForm.get.exp, newCardForm.get.securityCode, newCardForm.get.issueNu, newCardForm.get.start)
      cardDetails.add(newCard)
      Redirect(routes.BrowseController.categoryList)
  }
}
