package controllers

import javax.inject._

import models._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.Messages
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}
import play.api.Play.current
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages.Implicits._
import views.html.helper.form

@Singleton
class ReviewController @Inject() extends Controller {

  /**private val reviewForm : Form[Review] =
    Form(mapping(
      "Customer Name" -> text,
      "Product ID" -> text,
      "Review" -> nonEmptyText

    )(Review.apply)(Review.unapply))

  def saveCustomer = Action {
    implicit request =>
      val newCustomerForm = userForm.bindFromRequest()
      newCustomerForm.fold(hasErrors = {
        form =>
          Redirect(routes.RegistrationController.newCustomer()).flashing(Flash(form.data) + ("error" -> Messages("register.validation.errors")))
      }, success = {
        newCustomer =>
          Customer.add(newCustomer)
          Redirect(routes.LoginController.newLogin()).flashing("success" -> Messages("customers.new.success", newCustomer.firstName))}
      )
  }

  def newCustomer = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        userForm.bind(request2flash.data)
      else
        userForm
      Ok(views.html.registration(form))
  }**/

  /**def newReview(product: Product) = Action {
    implicit request =>
      val form = if(request2flash.get("error").isDefined)
        routes.ProductPageController.reviewForm.bind(request2flash.data)
      else
        reviewForm
      Ok(views.html.productPage(product,form))
  }


  def leaveReview(product: String) = Action {
    implicit request =>
      val newReviewForm = reviewForm.bindFromRequest()
      newReviewForm.fold(hasErrors = {
        form =>
          Redirect(routes.ProductPageController.goToProduct(product.name)).flashing(Flash(form.data) + ("error" -> Messages("review.validation.errors")))
      }, success = {
        newReview =>
          if (request.session.get("username").isEmpty) {
            Redirect(routes.LoginController.newLogin())
          } else {

            var clickedProduct = Product.findProductByName(product).get
            Review.add(newReview)
            Redirect(routes.ReviewController.newReview(clickedProduct))
          }
      }
      )
  }**/

  /**def sessionReviewCheck(product: String) = Action {

    implicit request =>
      if (request.session.get("username").isEmpty) {
        Redirect(routes.LoginController.newLogin())
      } else {

        var clickedProduct = Product.findProductByName(product).get

        Redirect(routes.ReviewController.leaveReview(clickedProduct))
      }
  }**/
}