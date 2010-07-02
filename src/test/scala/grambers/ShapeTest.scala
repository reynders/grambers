package grambers

import junit.framework._
import Assert._

class ShapeTest extends TestCase {

  def testPointEquality {
    assertEquals(Point(0,0), Point(0,0))
  }
  
  def testRectangleAsLines {
    val rectangle = new Rectangle(new Point(10, 10), 10, 10)
    val lines = rectangle.asLines
    
    assertEquals(new Line(5.0, 5.0, 15.0, 5.0), lines(0))
    assertEquals(new Line(15.0, 5.0, 15.0, 15.0), lines(1))
    assertEquals(new Line(15.0, 15.0, 5.0, 15.0), lines(2))
    assertEquals(new Line(5.0, 15.0, 5.0, 5.0), lines(3))
    
    // Check that equals works no matter which way the line is
    assertEquals(new Line(15.0, 5.0, 5.0, 5.0), lines(0))  
    assertFalse(new Line(0, 0, 0, 0) == lines(0))
  }
  
  def testRectangleWandH {
    val rectangle = Rectangle(0, 0, 9, 4)
    assertEquals(10.0, rectangle.w)
    assertEquals(5.0, rectangle.h)
  }

  def testRectangleOverlappingWhenNotColliding() {
    val r_1 = new grambers.Rectangle(new Point(2, 2), 2, 2)
    val r_2 = new grambers.Rectangle(new Point(5, 5), 2, 2)
    assertFalse(r_1.overlaps(r_2))
  }
    
  def testRectangleOverlapsWithWhenOverlapping() {
    val r_1 = new grambers.Rectangle(new Point(2, 2), 4, 4)      
    val r_2 = new grambers.Rectangle(new Point(3, 3), 2, 2)
    assertTrue(r_1.overlaps(r_2))
  }
    
  def testRectangleOverlappingWhenAdjacent() {
    val r_1 = new grambers.Rectangle(new Point(2, 2), 2, 2)      
    val r_2 = new grambers.Rectangle(new Point(4, 2), 2, 2)
    assertTrue(r_1.overlaps(r_2))
  }
  
  def testRectangleContains {
    val outerRectangle = Rectangle((0, 0), (3, 3))
    val completelyInnerRectangle = Rectangle((1, 1), (2, 2))
    assertEquals(true, outerRectangle.contains(completelyInnerRectangle))
    val partlyInnerRectangle = Rectangle((2, 2),(4, 4))
    assertEquals(false, outerRectangle.contains(partlyInnerRectangle))
    val sameSizeRectangle = Rectangle((0,0), (3,3))
    assertEquals(true, outerRectangle.contains(sameSizeRectangle))
  }
  
  def testDistanceFrom {
    var line = new Line(0, 0, -4, 0)
    assertEquals(2.0, line.distanceFrom(new Point(-2, 2)))
    
    line = new Line(0, 0, -1, 0)
    assertEquals(3.0, line.distanceFrom(new Point(-4, 0)))
    
    line = new Line(4, -4, -1, 0)
    assertEquals(2.0, line.distanceFrom(new Point(4, -6)))
    
    line = new Line(-2, 2, 2, 2)
    assertEquals(1.0, line.distanceFrom(new Point(0, 3)))
  }
  
  def testRectangleCollidesWithCircle {
    var box = new Rectangle(new Point(0, 0), 4, 4)
    var circle = new Circle(new Point(0, 3), 1)
    assertTrue(Shape.collidesWith(box, circle))
    
    circle = new Circle(new Point(0, 3), 0.99)
    assertFalse(Shape.collidesWith(box, circle))

    circle = new Circle(new Point(-4, 0), 2)
    assertTrue(Shape.collidesWith(box, circle))

    circle = new Circle(new Point(4, 0), 1.9)
    assertFalse(Shape.collidesWith(box, circle))
    
    circle = new Circle(new Point(3, 2.2), 2)
    assertTrue(Shape.collidesWith(box, circle))
  }
  
  def testRectangleFacingSide {
    var box = new Rectangle(new Point(0, 0), 4, 4)
    
    var circle = new Circle(new Point(0, 3), 1)
    assertEquals(box.side_up, box.facingSide(circle.center))
    
    circle = new Circle(new Point(0, -3), 1)
    assertEquals(box.side_down, box.facingSide(circle.center))
    
    circle = new Circle(new Point(3, 0), 1)
    assertEquals(box.side_right, box.facingSide(circle.center))
    
    circle = new Circle(new Point(-3, 0), 1)
    assertEquals(box.side_left, box.facingSide(circle.center))
  }
  
  def testCollisionUnitVector {
    var circle = new Circle(new Point(0, 0), 1)
    var box = new Rectangle(new Point(2, 0), 2, 2)
    var expectedVector = new Vector(-1, 0)
    assertEquals(expectedVector, Shape.collisionUnitVector(circle, box))
    assertEquals(expectedVector, Shape.collisionUnitVector(box, circle))
  }
}
