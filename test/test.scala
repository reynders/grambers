import scala.collection.mutable._

class Test {
  def currentTimeMillis : Long = // System.currentTimeMillis 
                                 System.nanoTime / 1000000

  def test1 {
    val myArray = for(i<-0 until 1000) yield(0)
    val t1 = currentTimeMillis
    for(x <-0 until 1000)
      for (y<-0 until 1000)
        doSomethingWith(myArray(y))
    val t2 = currentTimeMillis
    
    println("Test 1 result: " + (t2-t1) + "ms")
  }
  
  def test2 {
    val myArray = new Array[Int](1000)
    for(i<-0 until 1000) myArray(i) = 0
    
    val t1 = currentTimeMillis
    for(x <-0 until 1000)
      for (y<-0 until 1000)
        doSomethingWith(myArray(y))
    val t2 = currentTimeMillis
    
    println("Test 2 result: " + (t2-t1) + "ms")
  }
  
  def test3 {
    val myArray = new ArrayBuffer[Int](1000)
    for(i<-0 until 1000) myArray += 0
    
    val t1 = currentTimeMillis
    for(x <-0 until 1000)
      for (y<-0 until 1000)
        doSomethingWith(myArray(y))
    val t2 = currentTimeMillis
    
    println("Test 3 result: " + (t2-t1) + "ms")
  }

  def doSomethingWith(v:Int) {
    if (v == 1000)
      println(v)
  }
}

val test = new Test
test.test1
test.test1
test.test1
test.test2
test.test2
test.test2
test.test3
test.test3
test.test3

