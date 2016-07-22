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
/**
  * Created by Administrator on 14/07/2016.
  */
class SearchController @Inject() extends Controller {

//  val SearchForm: Form[SearchProduct] = Form(mapping(
//    "SearchIN" -> nonEmptyText.verifying("validation.name.nonexistant",
//      !SearchProduct.findByNameOB(_).isEmpty))(SearchProduct.apply)(SearchProduct.unapply)
//  )

  val SearchForm : Form[SearchProduct] = Form(mapping(
    "SearchIN" -> nonEmptyText.verifying("validation.name.nonexistant", !Product.findByNameS(_).isEmpty)
    )(SearchProduct.apply)(SearchProduct.unapply)
  )


  def listResult(Searched: String) = Action {
    implicit request =>
      Product.findByNameS(Searched).map {
        search =>
          val searchedProduct = Product.findByNameOB(Searched)
          println(Product.findByNameOB(Searched).size)
          Ok(views.html.listResult(searchedProduct))
      }.getOrElse(NotFound)
  }

//  def printForm () = {
//    val pp = SearchForm
//    for (print <-SearchForm) println(print)
//  }
  def saveSearch = Action {
    implicit request =>
      val newSearchForm = SearchForm.bindFromRequest()

      newSearchForm.fold(hasErrors = {
        form =>
          println("Faillllllllllll")
          Redirect(routes.HomeController.home()).flashing(Flash(form.data) +

            ("error" -> Messages("0 Results Found..")))

      }, success = {
        newSearch =>
          println("Successfullllllllllll" )
          Redirect(routes.SearchController.listResult(newSearch.name))}
      )
  }

  //pass form into home html in html document
  def newSearch = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        SearchForm.bind(request2flash.data)
      else
        SearchForm
      Ok(views.html.home())
  }

}
