package models


case class SearchProduct(name: String)

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
  //var nsearchproducts = Set(fill(Product.products, Set.empty[Product]))


    def fill(products: Set[SearchProduct], results: Set[SearchProduct]) : Set[SearchProduct] ={
      if (products.nonEmpty) {
        fill(products.tail, results + products.head)
        fill(products.tail, results)
      }
       else {
        results
      }
    }


  def findAll = searchproducts.toList.sortBy(_.name)

  def findByName(name: String) = searchproducts.toList.find(_.name contains(name))

  def findByNameOB(name: String) = {
    def filter(products: Set[SearchProduct], results: Set[SearchProduct]) : Set[SearchProduct] ={
      if (products.isEmpty)
        results
      else {
        if (products.head.name contains(name))
          filter(products.tail, results + products.head)
        else
          filter(products.tail, results)
      }
    }
    filter(searchproducts, Set.empty[SearchProduct]).toList
  }

}
