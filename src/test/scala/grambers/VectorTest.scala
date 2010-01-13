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
  
  def testAddAndSubstract {
    var vectorA = new Vector(2, 3)
    var vectorB = new Vector(-1, -1)
    var expectedVector = new Vector(1, 2)
    assertEquals(expectedVector, vectorA + vectorB)

    vectorA = new Vector(2, -2)
    vectorB = new Vector(-2, 2)
    expectedVector = new Vector(0, 0)
    assertEquals(expectedVector, vectorA + vectorB)
    
    vectorA = new Vector(2, 3)
    vectorB = new Vector(4, 1)
    expectedVector = new Vector(-2, 2)
    assertEquals(expectedVector, vectorA - vectorB)
    
    vectorA = new Vector(2, -2)
    vectorB = new Vector(-2, 2)
    expectedVector = new Vector(4, -4)
    assertEquals(expectedVector, vectorA - vectorB)

  }

  def testProjectionOn {
    var vectorA = new Vector(2, 2)
    var vectorB = new Vector(2, 0)
    var expectedProjection = new Vector(2, 0)
    assertEquals(expectedProjection, vectorA projectionOn vectorB)

    vectorA = new Vector(0, -4)
    vectorB = new Vector(4, -4)
    expectedProjection = new Vector(2, -2)
    assertEquals(expectedProjection, vectorA projectionOn vectorB)
    
    vectorA = new Vector(3, -2)
    vectorB = new Vector(0, -10)
    expectedProjection = new Vector(0, -2)
    assertEquals(expectedProjection, vectorA projectionOn vectorB)
  }
  
  def testNormalize {
    val vectorA = new Vector(10, -8)
    assertEquals(1.0, vectorA.unitVector.length)
  }
  /*
  def testNormals {
    var vectorA = new Vector(3, -1)
    var expectedVector = new Vector(-1, -3)
    assertEquals(expectedVector, vectorA leftHandNormal)
    expectedVector = new Vector(1, 3)
    assertEquals(expectedVector, vectorA rightHandNormal)
  }*/
  
  def testAngleBetween {
    var vectorA = new Vector(2, 2)
    var vectorB = new Vector(2, 0)
    assertEquals(45.0, rint(vectorA.angleBetween(vectorB)))

    vectorA = new Vector(-2, -2)
    vectorB = new Vector(-2, 0)
    assertEquals(45.0, rint(vectorA.angleBetween(vectorB)))
    
    vectorA = new Vector(2, 2)
    vectorB = new Vector(-2, 2)
    assertEquals(90.0, rint(vectorA.angleBetween(vectorB)))
    
    vectorA = new Vector(10, 10)
    vectorB = new Vector(-10, -10)
    assertEquals(180.0, rint(vectorA.angleBetween(vectorB)))
  }
  
  def testToString {
    val vector = new Vector(2.0, -4.0)
    vector.name = "TestVector"
    assertEquals("TestVector (2.0i, -4.0j)", vector.toString)
  }
}

