package models


/**
  * Created by Administrator on 12/07/2016.
  */
object DataDump {

  //some products
  var prods: Array[Product] = new Array[Product](4)
  /**prods(0) = Product("0001","Paperclips Large","Large Plain Pack of 1000", "100", "images/page3_img1.jpg", Set("images/big1.jpg"), "", "Lawnmower", "", Review.findReviewsByProductId("0001"))
  prods(1) = Product("0002","Giant Paperclips","Giant Plain 51mm 100 pack", "100", "images/page3_img2.jpg", Set("images/big2.jpg"), "", "Lawnmower","", Review.findReviewsByProductId("0002"))
  prods(2) = Product("0003","Paperclip Giant Plain", "Giant Plain Pack of 10000", "100", "images/page3_img3.jpg", Set("images/big3.jpg"), "", "Lawnmower","", Review.findReviewsByProductId("0003"))
  prods(3) = Product("0004","No Tear Paper Clip", "No Tear Extra Large Pack of 1000", "100", "images/page3_img4.jpg", Set("images/big4.jpg"), "", "Barbecues","", Review.findReviewsByProductId("0004"))
**/

  //a cart
  var cart1: Cart = new Cart(prods)
  var c1: Array[Cart] = new Array[Cart](1)
  c1(0) = cart1


  //another cart
  var cart2: Cart = new Cart(prods)
  var c2: Array[Cart] = new Array[Cart](1)
  c2(0) = cart2



  //orders
  var o1 = Order(1, "punks", c1, "26 JUL 16, 15:23", "Order Made", "Pay Now")
  var o2 = Order(2, "bigNoob", c2, "26 JUL 16, 15:23", "Order Made", "Pay Now")


  //set of orders
  var orders = List(o1, o2)



}
