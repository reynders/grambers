package grambers

import junit.framework._
import Assert._
import java.lang.Math._

class VectorTest extends TestCase {
  
  def testDot {
    val vectorA = new Vector(2, 2)
    var vectorB = new Vector(-1, -1)
    assertEquals(-4.0, vectorA dot vectorB)
    vectorB = new Vector(100, 0)
    assertEquals(200.0, vectorA dot vectorB)
  }
  
  def testLength {
    var vector = new Vector(-4, 3)
    assertEquals(5.0, vector.length)
  }

  def testNormalize {
    val vectorA = new Vector(10, -8)
    assertEquals(1.0, vectorA.normal.length)
  }
  
  def testAngleBetween {
    val vectorA = new Vector(2, 2)
    val vectorB = new Vector(2, 0)
    //val angle : Int = rint(vectorA.angleBetween(vectorB))
    assertEquals(45.0, rint(vectorA.angleBetween(vectorB)))
  }
  
  def testToString {
    val vector = new Vector(2.0, -4.0)
    vector.name = "TestVector"
    assertEquals("TestVector (2.0i, -4.0j)", vector.toString)
  }
}

