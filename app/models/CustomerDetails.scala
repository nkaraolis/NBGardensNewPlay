package models

import play.api.data.Forms._
import play.api.data._

/**
  * Created by Administrator on 06/07/2016.
  */
case class CustomerDetails(firstName: String, lastName: String, email: String, telephone: Int, username: String, password: String)

  object Customer {
    var customers = Set(CustomerDetails("Nick", "Noob", "MrNoob@everywhere.com", 999, "bigNoob", "mrPassword"))

    def add(customer : CustomerDetails): Unit ={
      customers = customers + customer
    }
  }

