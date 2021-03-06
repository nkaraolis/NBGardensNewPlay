package controllers

import javax.inject._

import models.Product
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

import scala.util.{Failure, Success}

/**
  * Created by Administrator on 08/07/2016.
  */
@Singleton
class ProductPageController extends Controller {

  private val reviewForm : Form[Review] =
    Form(mapping(
      "Username" -> text,
      "Review Title" -> nonEmptyText,
      "Review" -> nonEmptyText,
      "Review Date" -> nonEmptyText,
      "Rating" -> nonEmptyText
    )(Review.apply)(Review.unapply))


  def goToProduct(product: String) = Action {
    implicit request => //controller action

      if(Product.loadCheck) {
        println("loaded already")
      } else {
        println("loading products")
        Product.loadUpdatedProducts()
      }

      if (Product.findByName(product).isDefined) {

        val clickedProduct = Product.findByName(product).get

        val form = if (request2flash.get("error").isDefined)
          reviewForm.bind(request2flash.data)
        else
          reviewForm

        Ok(views.html.productPage(clickedProduct, form))

      } else {

        val clickedProduct = Product.findById(product.toInt).get

        val form = if (request2flash.get("error").isDefined)
          reviewForm.bind(request2flash.data)
        else
          reviewForm

        Ok(views.html.productPage(clickedProduct, form))
      }
  }


  def submitReview(product: String) = Action {
    implicit request =>
      val submitReviewForm = reviewForm.bindFromRequest()
      submitReviewForm.fold(hasErrors = {
        form =>
          Redirect(routes.ProductPageController.goToProduct(product)).flashing(Flash(form.data) + ("error" -> Messages("Error in review form")))
      }, success = {
        submitReview =>

          //Product.findById(product).get.reviews.isEmpty

          if(CustomerDB.findByUsername(submitReviewForm.data("Username")).isEmpty)
          {
            Redirect(routes.LoginController.newLogin())
          }
          else{

          //val currentCustomer = CustomerDB.findByUsername(request.session.get("username").get).head

            println("Products length" + Product.products.size)
            println("Old review size: " + Product.findById(product.toInt).get.reviews.length)

          Review.add(product.toInt,submitReview,"$addToSet")
            Thread.sleep(200)
            Product.loadUpdatedProducts()

            println("Products length" + Product.products.size)
            println("New review size: " + Product.findById(product.toInt).get.reviews.length)

            Redirect(routes.ProductPageController.goToProduct(product))
      }})
  }
}
