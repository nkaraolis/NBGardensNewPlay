package models

/**
  * Created by Administrator on 18/07/2016.
  */
case class  Category (catName: String, description: String, imgS: String, imgL: String)

object Category {
  var Categories = Set(
    Category("Lawnmower","Whatever the size of your lawn find the right lawnmower here", "images/page3_img1.jpg", "images/big1.jpg"),
    Category("Barbecues","Cook up a feast on one of our barbecues - shop the range here", "images/page3_img2.jpg", "images/big2.jpg"),
    Category("Furniture", "Browse our garden furniture sets & find the right one for your garden", "images/page3_img3.jpg", "images/big3.jpg")
  )

  def findAll = Categories.toList.sortBy(_.catName)

  def findCategoryByName(name: String) = Categories.find(_.catName == name)

  def findByName(name: String) = Categories.toList.find(_.catName == name)

  def add(name: String, description: String, imgS: String, imgL: String): Unit ={
    Categories += Category(name,description,imgS,imgL)
  }

  def removeFromCategory(category: Category): Set[Category] ={
    def checkOldList(categoryOld: Array[Category], category: Category): Array[Category] = {
      if (categoryOld.isEmpty) {
        categoryOld
      }
      else if (category.catName == categoryOld.head.catName) {
        checkOldList(categoryOld.tail, category)
      }
      else {
        checkOldList(categoryOld.tail, category) :+ categoryOld.head
      }
    }
    Categories = checkOldList(Categories.toArray, category).toSet
    Categories
  }
}