package models

/**
  * Created by Administrator on 18/07/2016.
  */
case class  Category (catName: String, description: String, imgS: String, imgL: String)

object Category {
  var Categories = Set(
    Category("Gnomes", "Browse NB Gardens' award winning gnome collection", "images/Gnomes.jpg", "images/Gnomes.jpg"),
    Category("Lawnmower","Whatever the size of your lawn find the right lawnmower here", "images/Lawnmowers.jpg", "images/Lawnmowers.jpg"),
    Category("Barbecues","Cook up a feast on one of our barbecues - shop the range here", "images/Barbecues.jpg", "images/Barbecues.jpg"),
    Category("Furniture", "Browse our garden furniture sets & find the right one for your garden", "images/Furniture.jpg", "images/Furniture.jpg"),
    Category("Garden Tools", "Explore our range of garden tools & gardening equipment", "images/Tools.jpg", "images/Tools.jpg"),
    Category("Gardening Supplies", "We have all the supplies you'll need to help maintain a healthy garden", "images/gardenSupplies.jpg", "images/gardenSupplies.jpg"),
    Category("Outdoor Equipment", "We have all the supplies you'll need to help maintain a healthy garden", "images/outdoorEquipment.jpg", "images/outdoorEquipment.jpg"),
    Category("Pots & Garden Ornaments", "Browse our range of sophisticated pots and ornaments", "images/ornaments.jpg", "images/ornaments.jpg")
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