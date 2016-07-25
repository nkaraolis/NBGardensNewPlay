package controllers

import play.api.mvc._
import models.{Cart, Customer, Order}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages

/**
  * Created by Administrator on 21/07/2016.
  */
class CheckoutController extends Controller{

  val orderForm: Form[Order] = Form(
    mapping(
      "ordId" -> of[Int],
      "cusId" -> of[String],
      "carts" -> of [Array[Cart]],
      "datetime" -> of[String],
      "status" -> of[String],
      "payMethod" -> of[String]
    )
    (Order.apply)(Order.unapply))


  def saveOrder = Action {
    implicit request =>
      val newOrderForm = orderForm.bindFromRequest()
      newOrderForm.fold(hasErrors = {
        form =>
          Redirect(routes.CheckoutController.newOrder()).flashing(Flash(form.data) + ("error" -> Messages("register.validation.errors")))
      }, success = {
        newCustomer =>
          Order.add(newOrder)
          Redirect(routes.PayController.pay()).flashing("success" -> Messages("customers.new.success", newOrder.cusId))}
      )
  }

  def newOrder = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        orderForm.bind(request2flash.data)
      else
        orderForm
      Ok(views.html.checkout(form))
  }


}
