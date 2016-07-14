package controllers
import javax.inject._

import models.{Customer, CustomerDetails, CustomerLogin}
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

  private val SearchForm: Form[Product] = Form(mapping(
    "Search" -> nonEmptyText.verifying("validation.email.")
  ))

  def save = Action {
    implicit request =>
      val newSearchForm = SearchForm.bindFromRequest()
      newSearchForm.fold(has Errors = {
        form =>
          Redirect(routes.SearchController.newSearch)
      }
  }
  def newSearch = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        SearchForm.bind(request2flash.data)
      else
        SearchForm
      Ok(views.html.home(form))
  }

}
