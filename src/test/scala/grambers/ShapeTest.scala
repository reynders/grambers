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
  
  def testDistanceFrom {
    var line = new Line(0, 0, -4, 0)
    assertEquals(2.0, line.distanceFrom(-2, 2))
    
    line = new Line(0, 0, -1, 0)
    assertEquals(3.0, line.distanceFrom(-4, 0))
    
    line = new Line(4, -4, -1, 0)
    assertEquals(2.0, line.distanceFrom(4, -6))
    
    line = new Line(-2, 2, 2, 2)
    assertEquals(1.0, line.distanceFrom(0, 3))
  }
  
  def testRectangleCollidesWithCircle {
    var box = new Rectangle(0, 0, 4, 4)
    var circle = new Circle(0, 3, 1)
    assertTrue(box.collidesWith(box, circle))
    
    circle = new Circle(0, 3, 0.99)
    assertFalse(box.collidesWith(box, circle))

    circle = new Circle(-4, 0, 2)
    assertTrue(box.collidesWith(box, circle))

    circle = new Circle(4, 0, 1.9)
    assertFalse(box.collidesWith(box, circle))
    
    circle = new Circle(3, 2.2, 2)
    assertTrue(box.collidesWith(box, circle))
  }
  
  def testRectangleFacingSide {
    var box = new Rectangle(0, 0, 4, 4)
    
    var circle = new Circle(0, 3, 1)
    assertEquals(box.side_up, box.facingSide(circle.x, circle.y))
    
    circle = new Circle(0, -3, 1)
    assertEquals(box.side_down, box.facingSide(circle.x, circle.y))
    
    circle = new Circle(3, 0, 1)
    assertEquals(box.side_right, box.facingSide(circle.x, circle.y))
    
    circle = new Circle(-3, 0, 1)
    assertEquals(box.side_left, box.facingSide(circle.x, circle.y))
  }
  
  def testCollisionUnitVector {
    var circle = new Circle(0, 0, 1)
    var box = new Rectangle(2, 0, 2, 2)
    var expectedVector = new Vector(-1, 0)
    assertEquals(expectedVector, circle.collisionUnitVector(circle, box))
    assertEquals(expectedVector, box.collisionUnitVector(box, circle))
  }
}
