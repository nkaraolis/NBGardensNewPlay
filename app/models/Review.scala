package models

import play.api.data.Forms._
import play.api.data._

case class Review(productId: String, custName: String, review: String) {

}
  object Review {
    var reviews = Set(Review("0001", "Fag", "Very good cheers"))

    def add(review: Review): Unit ={

      reviews = reviews + review
    }
  }