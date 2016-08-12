///**
//  * Created by Administrator on 09/08/2016.
//  */
//import models.{CustomerDB, MongoConnector}
//import org.scalatest.{FlatSpec, Matchers, Tag}
//import org.scalatest.Assertions._
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play._
//import play.api.test._
//import play.api.test.Helpers._
//import org.scalatest._
//import play.api.test._
//import play.api.test.Helpers._
//import org.scalatestplus.play._
//import play.api.data.Form
//import play.api.data.Forms._
//import play.api.i18n.Messages
//import play.api.mvc.{Action, Controller, Flash}
//import play.mvc.Results
//import reactivemongo.bson.BSONDocument
//
//import scala.collection.mutable
//import scala.concurrent.Future
//import scala.util.{Failure, Success}
//
//trait LoginController {
//  this: Controller =>
//
//  val LoginForm = Form(tuple(
//    "Username" -> nonEmptyText,
//    "Password" -> nonEmptyText))
//
//  def newLogin() = Action {
//    Ok("ok")
//  }
//
//}
//object LoginController extends Controller with LoginController
//
//
//class ExampleControllerSpec extends PlaySpec {
//
//  class TestController() extends Controller with LoginController
//
//  "Example Page#index" should {
//    "should be valid" in {
//      val controller = new TestController()
//      val result = controller.newLogin().apply(FakeRequest())
//      val bodyText: String = contentAsString(result)
//      bodyText mustBe "ok"
//    }
//  }
//}
//
//class LoginTest extends PlaySpec with OneAppPerTest{
//
//  "Find Customer Method" should {
//    "find a document of user with the correct username" in CustomerDB.findCustomer("BigNoob").username.nonEmpty
//  }
//
//  "Checking User Credentials" should {
//    "check if a username and password has a match" in LoginTest.this
//  }
//
//  "Find customer by username" should {
//    "find a customer document to that corresponding" in CustomerDB.findByUsername("BigNoob").nonEmpty
//  }
//
//  "If a customer enters a incorrect username" should{
//    "return an empty document" in CustomerDB.findByUsername("Tester").isEmpty
//  }
//
//  "When a user forgets their password use an email" should {
//    "find a customer document that has the same email" in CustomerDB.findByEmail("new@email.com").nonEmpty
//  }
//
//  "If a customer enters a incorrect email" should{
//    "return an empty document" in CustomerDB.findByUsername("Tester").isEmpty
//  }
//
//  "Auto increment a customer ID in MongoDB" should {
//    "add a new ID when a new customer instance is being created" in CustomerDB.findNextID()
//  }
//
//
//}
