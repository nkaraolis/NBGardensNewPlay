package ModelsTest

import models.{CustomerAddressDB, CustomerDB}
import org.scalatest.{FlatSpec, Matchers, Tag}
import reactivemongo.bson.Macros

/**
  * Created by Administrator on 2016/8/15.
  */
class CustomerAddressDBTests extends FlatSpec with Matchers {
  val addressBSONHandler = Macros.handler[CustomerAddressDB]
  val newAddress = CustomerAddressDB(999, "YYZZ", "aa", "bb", "cc", "dd", "ee", "ff")

  it should "return true if removeAddress" taggedAs ReturnRemoveAddress in {
    CustomerAddressDB.updateAddress("BenCosford2", newAddress, "$addToSet")
  }
}
object ReturnRemoveAddress extends Tag("removed Address")

