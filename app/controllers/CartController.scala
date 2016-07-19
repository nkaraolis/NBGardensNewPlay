package controllers

import play.api.mvc._
import models.{Cart, Product, aFormForCart}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._


class CartController extends Controller {

  val CartForm: Form[aFormForCart] = Form(
    mapping(
      "Product" -> of[String],
      "Qty" -> of[String]
    )
    (aFormForCart.apply)
    (aFormForCart.unapply)
  )

  var products: Array[Product] = Array.empty

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
      Redirect(routes.BrowseController.list) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def update() = Action {
    implicit request =>  //controller action
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get
      val q:String = CartForm.bindFromRequest().data("Qty")
      removeO(Product.findByName(CartForm.bindFromRequest().data("Product")).get.name)
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.imgS, p.imgL, q)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }
      val cp = Cart.addToCart(np)
      products =  cp //get product from model
      Ok(views.html.cartpage(products.toList, CartForm))//render view template
  }

  def removeO(product: String) : Array[Product] ={
    products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
    products
  }

}
