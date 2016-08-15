package ModelsTest

import models.ContactDB
import org.scalatest.{FlatSpec, Matchers, Tag}

/**
  * Created by Administrator on 2016/8/15.
  */
class ContactDBTests extends FlatSpec with Matchers {
  it should "return true if Item is found" taggedAs ReturnCIdSuccess in {
    (3) shouldEqual ContactDB.findNextID()
  }
}
object ReturnCIdSuccess extends Tag("ReturnIdSuccess")

