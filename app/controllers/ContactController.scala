package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class ContactController  @Inject() extends Controller{

  def contact = Action {
    Ok(views.html.contact())
  }

}