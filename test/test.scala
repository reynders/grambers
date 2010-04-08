import scala.collection.mutable._

object MyTestCollection extends ArrayBuffer {
  val value = 1
}

class Test {
  val myTestCollection = MyTestCollection

  def print(t : Test) : Int = {
    return t.myTestCollection.value
  }
}

val myTest = new Test

println(myTest.print(myTest))
