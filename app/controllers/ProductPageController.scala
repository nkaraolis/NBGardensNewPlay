package controllers

import javax.inject._

import models.{Product}
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
  * Created by Administrator on 08/07/2016.
  */

class ProductPageController extends Controller {

  private val reviewForm : Form[Review] =
    Form(mapping(
      "Product ID" -> nonEmptyText,
      "Username" -> text,
      "Review Title" -> nonEmptyText,
      "Review" -> nonEmptyText,
      "Review Date" -> nonEmptyText,
      "Rating" -> nonEmptyText
    )(Review.apply)(Review.unapply))

  def goToProduct(product: String) = Action {
    implicit request => //controller action

      if(Product.findByName(product).isDefined){

        val clickedProduct = Product.findByName(product).get

        val form = if(request2flash.get("error").isDefined)
          reviewForm.bind(request2flash.data)
        else
          reviewForm

        Ok(views.html.productPage(clickedProduct, form))

      }else{

        val clickedProduct = Product.findById(product).get

        val form = if(request2flash.get("error").isDefined)
          reviewForm.bind(request2flash.data)
        else
          reviewForm

        Ok(views.html.productPage(clickedProduct, form))
      }
  }

  def submitReview = Action {
    implicit request =>
      val submitReviewForm = reviewForm.bindFromRequest()
      submitReviewForm.fold(hasErrors = {
        form =>
          Redirect(routes.ProductPageController.goToProduct(submitReviewForm.data("Product ID"))).flashing(Flash(form.data) + ("error" -> Messages("Error in review form")))
      }, success = {
        submitReview =>

          if(CustomerDB.findByUsername(submitReviewForm.data("Username")).isEmpty)
          {
            Redirect(routes.LoginController.newLogin())
          }
          else{

          val currentCustomer = Customer.findCustomer(request.session.get("username").get)

          var newReview = new Review("","","","","","")

          if ((!(submitReviewForm.data("Review Title").length == 0)) && (!(submitReviewForm.data("Review").length == 0))) {

            newReview.productId = submitReviewForm.data("Product ID")
            newReview.username = currentCustomer.username

            if (!(submitReviewForm.data("Review Title").length == 0)) {
              newReview.reviewTitle = submitReviewForm.data("Review Title")
            }
            if (!(submitReviewForm.data("Review").length == 0)) {
              newReview.review = submitReviewForm.data("Review")
            }
            if (!(submitReviewForm.data("Review Date").length == 0)) {
              newReview.reviewDate = submitReviewForm.data("Review Date")
            }
            if (!(submitReviewForm.data("Rating").length == 0)) {
              newReview.rating = submitReviewForm.data("Rating")
            }

            Review.add(newReview)

            val reviewSession = request.session +
              ("Product ID" -> newReview.productId) +
              ("Username" -> newReview.username) +
              ("Review Title" -> newReview.reviewTitle) +
              ("Review" -> newReview.review) +
              ("Review Date" -> newReview.reviewDate) +
              ("Rating" -> newReview.rating)
            Redirect(routes.ProductPageController.goToProduct(newReview.productId)).withSession(reviewSession)
          }
          else{
            val reviewSession = request.session +
              ("Product ID" -> submitReviewForm.data("Product ID")) +
              ("Username" -> submitReviewForm.data("Username")) +
              ("Review Title" -> submitReviewForm.data("Review Title")) +
              ("Review" -> submitReviewForm.data("Review")) +
              ("Review Date" -> submitReviewForm.data("Review Date")) +
              ("Rating" -> submitReviewForm.data("Rating"))
            Redirect(routes.ProductPageController.goToProduct(newReview.productId)).withSession(reviewSession)
          }
      }})
  }
}
