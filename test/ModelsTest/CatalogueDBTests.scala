package ModelsTest

import models.{CatalogueDB}
import org.scalatest.{FlatSpec, Matchers, Tag}
/**
  * Created by Administrator on 2016/8/15.
  */
class CatalogueDBTests extends FlatSpec with Matchers {
  it should "return true if Item is found" taggedAs ReturnIdSuccess in {
    (3) shouldEqual CatalogueDB.findNextID()
  }
}
object ReturnIdSuccess extends Tag("ReturnIdSuccess")

