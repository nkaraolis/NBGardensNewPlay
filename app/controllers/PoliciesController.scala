package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash, Request}

/**
  * Created by Administrator on 06/07/2016.
  */
@Singleton
class PoliciesController @Inject() extends Controller{

  def policies = Action {
    implicit request =>
    Ok(views.html.policies())
  }

}
