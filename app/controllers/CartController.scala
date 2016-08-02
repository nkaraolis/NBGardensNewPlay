package controllers

import java.util.Calendar

import play.api.mvc._
import models.{Cart, Order, Product, aFormForCart}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
  * Created by Administrator on 08/07/2016.
  */

class CartController extends Controller {

  // a form contents information about a product in the cart
  val CartForm: Form[aFormForCart] = Form(
    mapping(
      "Product" -> of[String],
      "Qty" -> of[String],
      "sTotal" -> of[String]
    )
    (aFormForCart.apply)
    (aFormForCart.unapply)
  )

  // products in the cart for the user
  var products: Array[Product] = Array.empty
  var totalT = 0.00

  // return products in the cart
  def list = Action {
    implicit request =>  //controller action
      products = Cart.productsInCart  //get product from model
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  def add(product: String, Qty: String) = Action {
    implicit request =>  //controller action

      // find the product in all products
      val p = Product.findByName(product).get

      // create a new product, the new product is the found product but with Qty, and add the product to the cart
      def np : Product = {
        Product.add(p.productId, p.name, p.description, p.price, p.mainImage, p.secondaryImages, Qty, p.category, p.porousAllowed, p.reviews)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }

      // update products in cart
      products = Cart.addToCart(np) //get product from model
      Redirect(routes.BrowseController.productList(p.category)) //render view template
  }

  def remove(product: String) = Action {
    implicit request =>  //controller action
      products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
      removeOldPrice(Product.findByName(product).get.name)
      Ok(views.html.cartpage(products.toList, CartForm)) //render view template
  }

  // update qty from the cart page
  def update() = Action {
    implicit request =>  //controller action

      // get the current product in the cart which the user wants to change
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get

      // get the new qty for the product
      val q:String = CartForm.bindFromRequest().data("Qty")

      // calculate the new total for the product
      val subTot = (q.toDouble) * (p.price.toDouble)

      // call the renewTotalPrice function to have a new total price for the user (see renewTotalPrice)
      removeOldPrice(p.name)

      // remove the out of date product from the cart list
      removeO(p.name)

      // create a new product, the new product is the found product but with new Qty, and add the product to the cart
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.mainImage, p.secondaryImages, q, p.category, p.porousAllowed, p.reviews)
        val t = Product.findByName(p.name).toArray.apply(0)
        t
      }
      val cp = Cart.addToCart(np)

      // update products in cart
      products =  cp //get product from model
      totalT += subTot
      Ok(views.html.cartpage(products.toList, CartForm))//render view template
  }

  // update qty from the products page, the same with above function
  def updateFromPL() = Action {
    implicit request =>  //controller action
      val p = Product.findByName(CartForm.bindFromRequest().data("Product")).get
      val q:String = CartForm.bindFromRequest().data("Qty")
      val subTot = (q.toDouble) * (p.price.toDouble)
//      if (totalT!=0){
//        removeOldPrice(p.name)
//         }
      removeO(p.name)
      def np : Product = {
        Product.removeFromProduct(p)
        Product.add(p.productId, p.name, p.description, p.price, p.mainImage, p.secondaryImages, q, p.category, p.porousAllowed, p.reviews)
        val t = Product.findByName(p.name).toArray.apply(0)//
        t
      }
      val cp = Cart.addToCart(np)
      products =  cp //get product from model
      totalT += subTot
      Redirect(routes.BrowseController.productList(p.category))//render view template
  }

  def checkout() = Action {
    implicit request =>  //controller action
      if (request.session.get("username").isEmpty) {//check the user has logged in or not
        Redirect(routes.LoginController.newLogin())
      } else {
        totalT = 0.00
        for (i<-products){
          totalT += (i.qty.toDouble*i.price.toDouble)
        }
        Ok(views.html.checkout(products.toList, totalT)) //render view template
      }
  }

  def removeO(product: String) : Array[Product] ={
    products = Cart.removeFromCart(Product.findByName(product).get)   //get product from model
    products
  }

  def removeOldPrice(product: String) : Double ={
    totalT = totalT - (Product.findByName(product).get.price.toDouble * Product.findByName(product).get.qty.toDouble)
    totalT
  }





}
