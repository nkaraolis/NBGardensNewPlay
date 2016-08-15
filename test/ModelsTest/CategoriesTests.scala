package ModelsTest

import models.Category
import org.scalatest.{FlatSpec, Matchers, Tag}

/**
  * Created by Administrator on 2016/8/15.
  */
class CategoriesTests extends FlatSpec with Matchers {
  it should "return true if removeFromCategory Ok" taggedAs removeFromCategory in {
    (7) shouldEqual Category.removeFromCategory(Category("Gnomes", "Browse NB Gardens' award winning gnome collection", "images/Gnomes.jpg", "images/Gnomes.jpg")).size
  }
}
object removeFromCategory extends Tag("removeOk")

