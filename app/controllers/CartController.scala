package controllers

import java.util.Calendar

import play.api.mvc._
import models.{Cart, Order, Product, aFormForCart}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._


class CartController extends Controller {

  val CartForm: Form[aFormForCart] = Form(
    mapping(
      "Product" -> of[String],
      "Qty" -> of[String],
      "sTotal" -> of[String]
    )
    (aFormForCart.apply)
    (aFormForCart.unapply)
  )

  var products: Array[Product] = Array.empty
  var totalT = 0.00

  def list = Action {
    implicit request =>  //controller action
      products = Cart.productsInCart  //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def add(product: String, Qty: String) = Action {
    implicit request =>  //controller action
      val p = Product.findByName(product).get
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.imgS, p.imgL, Qty)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }
      products = Cart.addToCart(np) //get product from model
      Redirect(routes.BrowseController.productList) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      renewTotalPrice(Product.findByName(product).get.name)
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def update() = Action {
    implicit request =>  //controller action
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get
      val q:String = CartForm.bindFromRequest().data("Qty")
      val subTot = (CartForm.bindFromRequest().data("Qty").toDouble) * (p.price.toDouble)
      removeO(Product.findByName(CartForm.bindFromRequest().data("Product")).get.name)
      renewTotalPrice(Product.findByName(CartForm.bindFromRequest().data("Product")).get.name)
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.imgS, p.imgL, q)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }
      val cp = Cart.addToCart(np)
      products =  cp //get product from model
      totalT += subTot
      Ok(views.html.cartpage(products.toList, CartForm))//render view template
  }

  def checkout() = Action {
    implicit request =>  //controller action
      Ok(views.html.checkout(products.toList, totalT))//render view template
  }

  def updateFromPL() = Action {
    implicit request =>  //controller action
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get
      val q:String = CartForm.bindFromRequest().data("Qty")
      val subTot = (CartForm.bindFromRequest().data("Qty").toDouble) * (p.price.toDouble)
      removeO(Product.findByName(CartForm.bindFromRequest().data("Product")).get.name)
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.imgS, p.imgL, q)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }
      val cp = Cart.addToCart(np)
      products =  cp //get product from model
      totalT += subTot
      Redirect(routes.BrowseController.productList)//render view template
  }

  def removeO(product: String) : Array[Product] ={
    products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
    products
  }

  def renewTotalPrice(product: String) : Double ={
    totalT = totalT - (Product.findByName(product).get.price.toDouble * Product.findByName(product).get.need.toDouble)
    totalT
  }


  def save(cusId:String, payMethod:String) = Action {
    implicit request =>
//      val carts = CartForm.bindFromRequest()
//      val now = Calendar.getInstance().getTime()
//      var status = "Order Created"


      var order = Order(1, cusId, _, _, "Order placed", payMethod.toString)

  }





}
