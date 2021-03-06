package controllers

import javax.inject.Singleton

import play.api.mvc.{Action, Controller}
import models.{Category, Product}

/**
  * Created by Administrator on 08/07/2016.
  */
@Singleton
class BrowseController extends Controller{


  // call a products list in a category
  def productList(cat: String) = Action {
    implicit request =>  //controller action

      if(Product.loadCheck) {
        println("loaded already")
      } else{
        Product.loadProducts()
      }
      val products = Product.findByCat(cat)  //get product from model
      Ok(views.html.ProductList(products)) //render view template
  }


  // call all categories
  def categoryList = Action {
    implicit request =>  //controller action

      if(Product.loadCheck) {
        println("loaded already")
      }else{
        Product.loadProducts()
      }
      val categories = Category.findAll  //get product from model
      Ok(views.html.browseCat(categories)) //render view template
  }





}
