package grambers
import junit.framework._
import Assert._

class UniverseTest extends TestCase {

    def testToRadians() {
      import java.lang.Math
      assertEquals(0.0, Math.toRadians(0))
      assertEquals(Math.round(Math.PI), Math.round(Math.toRadians(180)))
    }
    
    def testListIteration {
      val listOfThings = new Array[String](0)
      val circle = new grambers.Circle(1, java.awt.Color.red)
      val doh = List(circle)
      doh.foreach(println)
    }
    
    def testScalaCollectionPerformance {
      val arrayList = new java.util.ArrayList[Int](1)
      val scalaList = List(1)
      
      val t1 = System.currentTimeMillis()
      for (i <- 0 to 10000) {
        val number = i
        arrayList.add(i)
      }
      val t2 = System.currentTimeMillis()

      val t3 = System.currentTimeMillis()
      for (i <- 0 to 10000) {
        val number = i
        i::scalaList
      }
      val t4 = System.currentTimeMillis()
      println("arrayList: " + (t2-t1) + "ms, scalaList: " + (t4-t3) + "ms")
    }      
      
}
