import models.{Cart, CartItem, Product, Review}
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import play.api.test._

//some TDD tests for the Cart class
class CartTests extends FunSuite  with BeforeAndAfter {

  //Start with an empty cart and some variables
  var cart = Cart
  var review: Review = _
  var reviews: List[Review] = _
  var product: Product = _
  var cartitem: CartItem = _
  var review2: Review = _
  var product2: Product = _
  var cartitem2: CartItem = _
  var cTotal: Double = _

  before {
        //populate and assign the variables
    review = Review("Jackie", "Awesome product", "This gnome is great!", "11 JUL 2016", "5")
    reviews = List(review)
    product = Product(1, "really scary gnome", "This is a really scary gnome, it will frighten everyone away", "12.99", "main-image", "secondary-image", 1, "Gnomes", "yes", reviews)
    cartitem = CartItem(product.productId, product.name, 1, product.price.toDouble, true)
    review2 = Review("Jackie", "A Great Shed", "The shed was very easy to put up", "11 JUL 16", "5")
    reviews = List(review, review2)
    product2 = Product(2, "a lovely shed", "This is a lovely shed to store your tools in", "40.99", "main-image", "secondary-image", 1, "Garden Tools", "no", reviews)
    cartitem2 = CartItem(product2.productId, product2.name, 1, product2.price.toDouble, false)
  }


  //Test 1 to check the cart is empty
  test("Check the new cart currently holds no products"){
  var zero = 0
  var size = cart.productsInCart.length
  assert(size == zero)
  }


  //Test 2 add a product to a cart and check the length of the productsInCarts array is 1
  test("Check the cart now holds one product"){
  cart.addToCart(cartitem)
  var size = cart.productsInCart.length
  assert (size == 1)
  }


  //Test 3 increase the quantity and check again the contents of the cart
  test("Check the cart now holds two products"){
    cart.addToCart(cartitem2)
    var size = cart.productsInCart.length
    assert (size == 2)
  }

  //Test 4 calculate cart total
  test("Calculate the cart total"){
    val total = 12.99 + 40.99
    cTotal = cart.calculateCartTotal()
    println ("This is cTotal: "+ cTotal)
    assert(total == cTotal)
  }

  //Test 5 remove a product from a cart
  test("Remove a product from a cart"){
    cart.removeFromCart(cartitem2)
    var size = cart.productsInCart.length
    assert (size == 1)
  }

  //Test 6 clear all products from a cart
  test("Clear all products from the cart"){
    cart.clearCart()
    var size = cart.productsInCart.length
    assert (size == 0)
  }



}
