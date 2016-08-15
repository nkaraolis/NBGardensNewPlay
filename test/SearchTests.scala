import controllers.SearchController
import models.{Product}
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.collection.mutable.ArrayBuffer


//some TDD tests for the SearchController class
class SearchTests extends FunSuite with BeforeAndAfter {

//  //Start with some variables
//  var review: Review = _
//  var reviews: List[Review] = _
//  var product: Product = _
//  var review2: Review = _
//  var product2: Product = _
//  var products: Set[Product]  = Set(product, product2)
//
//  before {
//    //populate and assign the variables
//    review = Review("Jackie", "Awesome product", "This gnome is great!", "11 JUL 2016", "5")
//    reviews = List(review)
//    product = Product(1, "really scary gnome", "This is a really scary gnome, it will frighten everyone away", "12.99", "main-image", "secondary-image", 1, "Gnomes", "yes", reviews)
//    Product.add(1, "really scary gnome", "This is a really scary gnome, it will frighten everyone away", "12.99", "main-image", "secondary-image", 1, "Gnomes", "yes", reviews)
//    review2 = Review("Jackie", "A Great Shed", "The shed was very easy to put up", "11 JUL 16", "5")
//    reviews = List(review, review2)
//    product2 = Product(2, "a lovely shed", "This is a lovely shed to store your tools in", "40.99", "main-image", "secondary-image", 1, "Garden Tools", "no", reviews)
//    Product.add(2, "a lovely shed", "This is a lovely shed to store your tools in", "40.99", "main-image", "secondary-image", 1, "Garden Tools", "no", reviews)
//  }


  //Test 1 to check that the search functionality works
  test("Check if the search keyword 'Lantern Gnome' results in the Lantern Gnome within the database"){
    var search = "Lantern Gnome"
    val foundProduct = Product.findByNameOB(search)
    val name = foundProduct.head.name
    assert(name contains search)
  }







}
