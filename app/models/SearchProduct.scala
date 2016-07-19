package models


case class SearchProduct(Name: String)

object SearchProduct {
  var searchproducts = Set(
    SearchProduct("Paperclips Large"),
    SearchProduct("Giant Paperclips"),
    SearchProduct("Paperclip Giant Plain"),
    SearchProduct("No Tear Paper Clip"),
    SearchProduct("Zebra Paperclips"),
    SearchProduct( "Giant Plain Pack of 10000"),
    SearchProduct("No Tear Extra Large Pack of 1000"),
    SearchProduct("Zebra Length 28mm Assorted 150 Pack"),
    SearchProduct("Zebra Length 28mm Assorted 150 Pack"),
    SearchProduct("Zebra Length 28mm Assorted 150 Pack")
  )

  def findAll = searchproducts.toList.sortBy(_.Name)

  def findByName(name: String) = searchproducts.toList.find(_.Name == name)




}
