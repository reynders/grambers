package grambers
import junit.framework._
import Assert._
import org.jmock.Mockery
import org.jmock.Expectations._


class UniverseTest extends TestCase {

    val context = new Mockery()

    def testToRadians() {
      import java.lang.Math
      assertEquals(0.0, Math.toRadians(0))
      assertEquals(Math.round(Math.PI), Math.round(Math.toRadians(180)))
    }
    
    def testListIteration {
      val listOfThings = new Array[String](0)
      val circle = new grambers.Circle(1)
      val doh = List(circle)
      doh.foreach(println)
    }
    
    def testCollideThings {
      val universe = new Universe(100, 100)
      val box_1 = new grambers.Box(2, 2)      
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (4, 2)
      val box_3 = new grambers.Box(3, 3)
      box_3.location = (6, 6)

      universe.things += box_1
      universe.things += box_2
      universe.things += box_3

      universe.collideThings
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
