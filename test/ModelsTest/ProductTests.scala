package ModelsTest

import models.Product
import org.scalatest.{FlatSpec, Matchers, Tag}


//some TDD tests for the SearchController class
class ProductTests extends FlatSpec with Matchers {

  it should "return true if Item is found" taggedAs ReturnSearchSuccess in {
    ("Iowa 6 Burner Gas BBQ") shouldEqual Product.findByNameOB("Iowa 6 Burner Gas BBQ").head.name
  }

  it should "return true if the text is shorter" taggedAs ReturnShorter in {
    ('6') shouldEqual Product.preview("Iowa 6 Burner Gas BBQ", 5).charAt(5)
  }

}
object ReturnSearchSuccess extends Tag("ReturnSearchFoundSuccess")
object ReturnShorter extends Tag("ReturnShorterSuccess")
