import scala.collection.mutable._

class Test {
  def test {
    val myArray = new Array[Int](5)
    println("Test 1 result: " + myArray(0))
  }
  
  def test2 {
    val myArray = new ArrayBuffer[Int](5)
    println("Test 2 result: " + myArray(0))
  }

}

val test = new Test
test.test
test.test2
