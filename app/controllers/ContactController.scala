package controllers

import javax.inject._

import models.{ContactDB, MongoConnector}
import play.api._
import play.api.i18n.Messages
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{mapping, nonEmptyText}


import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class ContactController  @Inject() extends Controller{

  val contactForm = Form(tuple(
    "Name" -> text,
      "Email" -> text,
      "Message" -> text
    ))

  def saveContact = Action {
    implicit request =>
      val newContactForm = contactForm.bindFromRequest()
      newContactForm.fold(success = {
        newContact =>
          //Create a contact object using form data
          val newContactID = ContactDB.findNextID()
          print(newContactID)
          println(newContact._2)
          val newContactRequest = new ContactDB(newContactID, newContact._1, newContact._2, newContact._3)

          //Insert into contact collection
          MongoConnector.collectionContact.insert(newContactRequest)
          Ok(views.html.home())
      }, hasErrors = {
        form =>
          Redirect(routes.ContactController.contact()).flashing(Flash(form.data) + ("error" -> Messages("contact.validation.errors")))
      })
      }

  def contact = Action {
    implicit request =>
      val form = if (request2flash.get("error").isDefined)
        contactForm.bind(request2flash.data)
      else
        contactForm
      Ok(views.html.contact(form))
  }


}