import models.{Cart, DataDump, Product}
import org.scalatest.FunSuite


class TestingCart extends FunSuite{

  var prod1 = Product("0001","Paperclips Large","Large Plain Pack of 1000", "£100", "images/page3_img1.jpg", "images/big1.jpg")
  Cart(DataDump.getCart)

  println(Cart)

  Cart.removeFromCart(DataDump.getCart: Array[Product], prod1)

  println("We have removed the first product")

  println(Cart)



}
