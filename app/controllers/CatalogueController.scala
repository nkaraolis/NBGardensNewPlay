package controllers

import javax.inject._

import models.{CatalogueDB, ContactDB, MongoConnector}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class CatalogueController @Inject() extends Controller{

  val catalogueForm = Form(tuple(
      "Name" -> nonEmptyText,
      "Email" -> nonEmptyText,
      "Address Line 1" -> nonEmptyText,
      "Address Line 2" -> nonEmptyText,
      "Town/City" -> nonEmptyText,
      "Postcode" -> nonEmptyText
    ))

  def saveCatalogue = Action {
    implicit request =>
      val newCatalogueForm = catalogueForm.bindFromRequest()
      newCatalogueForm.fold(success = {
        newCatalogue =>
          //Create a contact object using form data
          val newCatalogueID = CatalogueDB.findNextID()
          print(newCatalogueID)
          println(newCatalogue._2)
          val newCatalogueRequest = new CatalogueDB(newCatalogueID, newCatalogue._1, newCatalogue._2, newCatalogue._3, newCatalogue._4, newCatalogue._5, newCatalogue._6)

          //Insert into contact collection
          MongoConnector.collectionCatalogue.insert(newCatalogueRequest)
          Ok(views.html.home())
      }, hasErrors = {
        form =>
          Redirect(routes.CatalogueController.catalogue()).flashing(Flash(form.data) + ("error" -> Messages("catalogue.validation.errors")))
      })
  }

  def catalogue = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        catalogueForm.bind(request2flash.data)
      else
        catalogueForm
      Ok(views.html.catalogue(form))
  }


}
