package grambers

import junit.framework._
import Assert._

class ShapeTest extends TestCase {

  def testRectangleAsLines {
    val rectangle = new Rectangle(10, 10, 10, 10)
    val lines = rectangle.asLines
    
    assertEquals(new Line(5.0, 5.0, 15.0, 5.0), lines(0))
    assertEquals(new Line(15.0, 5.0, 15.0, 15.0), lines(1))
    assertEquals(new Line(15.0, 15.0, 5.0, 15.0), lines(2))
    assertEquals(new Line(5.0, 15.0, 5.0, 5.0), lines(3))
    
    // Check that equals works no matter which way the line is
    assertEquals(new Line(15.0, 5.0, 5.0, 5.0), lines(0))
    
    assertFalse(new Line(0, 0, 0, 0) == lines(0))
  }

  def testRectangleOverlappingWhenNotColliding() {
    val r_1 = new grambers.Rectangle(2, 2, 2, 2)

    val r_2 = new grambers.Rectangle(5, 5, 2, 2)
    assertFalse(r_1.overlaps(r_2))
  }
    
  def testRectangleOverlapsWithWhenOverlapping() {
    val r_1 = new grambers.Rectangle(2, 2, 4, 4)      
    val r_2 = new grambers.Rectangle(3, 3, 2, 2)
    assertTrue(r_1.overlaps(r_2))
  }
    
  def testOverlappingWhenAdjacent() {
    val r_1 = new grambers.Rectangle(2, 2, 2, 2)      
    val r_2 = new grambers.Rectangle(4, 2, 2, 2)
    assertTrue(r_1.overlaps(r_2))
  }
    
  def testNormalBetweenCircleAndRectangle {
    val r = new Rectangle(6, 6, 4, 4)
    val c = new Circle(2, 2, 1)
    
    r.normal(c)
    assertTrue(r != c)
  }  
  
  def testLineNormal {
    var line = new Line(0, 0, 4, -4)
    var circle = new Circle(0, -4, 1)
    assertEquals(new Vector(2, -2), line.projectionOn(circle))

    circle = new Circle(0, 4, 1)    
    assertEquals(new Vector(-2, 2), line.projectionOn(circle))

    circle = new Circle(0, -6, 1)    
    assertEquals(new Vector(3, -3), line.projectionOn(circle))        
  }
  
  def testDistanceFrom {
    var line = new Line(0, 0, -4, 0)
    var circle = new Circle(-2, 2, 1)
    assertEquals(2.0, line.distanceFrom(circle))
    
    line = new Line(0, 0, -1, 0)
    circle = new Circle(-4, 0, 1)
    assertEquals(3.0, line.distanceFrom(circle))
    
    line = new Line(4, -4, -1, 0)
    circle = new Circle(4, -6, 1)
    assertEquals(2.0, line.distanceFrom(circle))

  }
}
