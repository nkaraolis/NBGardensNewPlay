package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.{Action, Controller, Flash}

/**
  * This controller is to able users to log in
  */
@Singleton
class LoginController @Inject() extends Controller {




  // def findCustomer(cusId:String) = customers.find(_.username == cusId)//dont want this one
  //this is a comment

// def findCustomer(cusId:String) =  C:Users.Administrator.IdeaProjects.NBGardensPlay2.NBGardensWebsite.app.models.datadump.getCustomers.find(_.username == cusId)



 /* def login(username:String, password:String) {
    val user = findCustomer(username).get
    print(user)
    if(user.password == password) {
      //log the user in
      print("@username is looged in")
    }  /*else {
      print("The password you have entered is incorrect")
    }*/
  }
*/

  def index = Action {
    Ok(views.html.home())
  }



}