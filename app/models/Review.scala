package models

import play.api.data.Forms._
import play.api.data._

case class Review(var productId: String, var username: String,var reviewTitle: String, var review: String, var reviewDate: String, var rating: String) {

}
  object Review {
    var reviews = Set(Review("0001", "BenCosford", "Very good product", "No complaints, would recommend /10", "27/06/2016","5"),
      Review("0001","DamnDaniel", "Not really to my liking if I'm honest.", "The paperclips were too large for me, personally. WHAT ARE THEY PAPERCLIPS FOR ELEPHANTS?! crap/10", "27/07/2016","1"))

    def add(review: Review): Unit ={

      reviews = reviews + review
    }

    def findReviewsByProductId(productId: String) = reviews.filter(_.productId == productId)
  }