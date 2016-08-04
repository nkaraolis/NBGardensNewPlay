package models


import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import play.api.data.Forms._
import play.api.data._

case class Review(username: String, reviewTitle: String, review: String, reviewDate: String, rating: String)


  object Review {
    //var reviews = Set(Review("0001", "BenCosford", "Very good product", "No complaints, would recommend /10", "27/06/2016","5"),
      //Review("0001","DamnDaniel", "Not really to my liking if I'm honest.", "The paperclips were too large for me, personally.", "27/07/2016","1"))

    var reviewList: List[BSONDocument] = List[BSONDocument]()

    implicit object reviewReader extends BSONDocumentReader[Review]{
      def read(doc: BSONDocument):Review = Review(
        doc.getAs[String]("username").get,
        doc.getAs[String]("reviewTitle").get,
        doc.getAs[String]("review").get,
        doc.getAs[String]("reviewDate").get,
        doc.getAs[String]("rating").get)
    }

    implicit object reviewWriter extends BSONDocumentWriter[Review] {
      def write(review: Review) : BSONDocument = BSONDocument(
        "username" -> review.username,
        "reviewTitle" -> review.reviewTitle,
        "review" -> review.review,
        "reviewDate" -> review.reviewDate,
        "rating" -> review.rating
      )
    }

    def add(productId: String, review: Review, updater: String): Unit ={

      val selector = BSONDocument("productId" -> productId)
      val modifier = BSONDocument(updater -> BSONDocument("reviews" -> review))
      val runUpdate = MongoConnector.collectionProduct.update(selector, modifier)

      runUpdate onComplete {
        Success =>
          println("Reviews updated")
      }
      Thread.sleep(500)
    }

    /** Update customer address **/
    def updateAddress(username: String, value: CustomerAddressDB, updater: String): Unit = {
      // Finds the user to update
      val selector = BSONDocument("username" -> username)
      // Sets the field to update to be addresses
      val modifier = BSONDocument(updater -> BSONDocument("addresses" -> value))
      // Runs the update query
      val runUpdate = MongoConnector.collectionCustomer.update(selector, modifier)
      runUpdate onComplete {
        Success =>
          println("Address updated")
      }
      Thread.sleep(500)
    }

    def findReviewsByProductId(productId: String): List[BSONDocument] = {
      // = reviews.filter(_.productId == productId)
      val findQuery = BSONDocument(
        "productId" -> productId
      )

      val foundReviews = MongoConnector.collectionProduct.find(findQuery).cursor[BSONDocument]().collect[List]()
      foundReviews.onComplete {
        case Failure(e) => throw e
        case Success(readResult) =>
          reviewList = readResult
      }
      Thread.sleep(500)
      reviewList
    }
  }