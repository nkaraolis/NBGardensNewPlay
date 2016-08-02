package models

import play.api.data.Forms._
import play.api.data._
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

case class Review(var productId: String, var username: String,var reviewTitle: String, var review: String, var reviewDate: String, var rating: String) {

}
  object Review {
    var reviews = Set(Review("0001", "BenCosford", "Very good product", "No complaints, would recommend /10", "27/06/2016","5"),
      Review("0001","DamnDaniel", "Not really to my liking if I'm honest.", "The paperclips were too large for me, personally.", "27/07/2016","1"))

    def add(review: Review): Unit ={

      reviews = reviews + review
    }

    implicit object reviewReader extends BSONDocumentReader[Review]{
      def read(doc: BSONDocument):Review = Review(
        doc.getAs[String]("productId").get,
        doc.getAs[String]("username").get,
        doc.getAs[String]("reviewTitle").get,
        doc.getAs[String]("review").get,
        doc.getAs[String]("reviewDate").get,
        doc.getAs[String]("rating").get)
    }

    implicit object ProductWriter extends BSONDocumentWriter[Review] {
      def write(review: Review) : BSONDocument = BSONDocument(
        "productId" -> review.productId,
        "username" -> review.username,
        "reviewTitle" -> review.reviewTitle,
        "review" -> review.review,
        "reviewDate" -> review.reviewDate,
        "rating" -> review.rating
      )
    }

    def findReviewsByProductId(productId: String) = reviews.filter(_.productId == productId)
  }