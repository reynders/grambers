package grambers

import junit.framework._
import Assert._

class ShapeTest extends TestCase {

  def testPointEquality {
    assertEquals(Point(0,0), Point(0,0))
  }

  def testPointAdditionAndSubstraction {
    assertEquals(Point(2,2), Point(1,1) + Point(1, 1))
    assertEquals(Point(0,0), Point(1,1) - Point(1, 1))
    assertEquals(Point(2,2), Point(1,1) + 1)
    assertEquals(Point(0,0), Point(1,1) - 1)
  }

  def testRectangleCreation {
    assertEquals(Rectangle((-2, -2), (3, 3)), Rectangle(Rectangle((0, 0), (1, 1)), 2))
  }

  def testRectangleDimensions {
    val r = Rectangle((-2, -2),(2, 2))
    assertEquals(-2, r.minX)
    assertEquals(2, r.maxY)
  }

  def testRectangleIntersects {
    val expected = new Rectangle((0, 3), (2, 5))
    assertEquals(expected, new Rectangle((0, 0), (10, 10)).intersect(
                           new Rectangle((-1, 3), (2, 5))))
  }

  def testRectangleLimitBy {
    val original = Rectangle((-1, -2), (4, 4))
    val limitBy = Rectangle((0,0), (5,5))
    assertEquals(Rectangle((0, 0), (4, 4)), original.limitBy(limitBy))
  }

  def testRectangleAsLines {
    val rectangle = new Rectangle(new Point(10, 10), 10, 10)
    val lines = rectangle.asLines

    assertEquals(new Line(5, 5, 15, 5), lines(0))
    assertEquals(new Line(15, 5, 15, 15), lines(1))
    assertEquals(new Line(15, 15, 5, 15), lines(2))
    assertEquals(new Line(5, 15, 5, 5), lines(3))

    // Check that equals works no matter which way the line is
    assertEquals(new Line(15, 5, 5, 5), lines(0))
    assertFalse(new Line(0, 0, 0, 0) == lines(0))
  }

  def testRectangleWandH {
    val rectangle = Rectangle(0, 0, 9, 4)
    assertEquals(9, rectangle.w)
    assertEquals(4, rectangle.h)
  }

  def testRectangleTranslate {
    assertEquals(Rectangle((0, 0), (5, 5)), Rectangle((5, 6), (10, 11)).translate(-5, -6))
  }

  def testIsSameSize {
    assertEquals(true, Rectangle((-5, -5), (5, 5)).isSameSize(Rectangle((0, 0), (10, 10))))
  }

  def testRectangleFitsIn {
    assertEquals(true, Rectangle((-3, -3), (3, 3)).fitsIn(Rectangle((0, 0), (7, 7))))
    assertEquals(false, Rectangle((0, 0), (7, 7)).fitsIn(Rectangle((-3, -3), (3, 3))))
    assertEquals(true, Rectangle((-3, -3), (3, 3)).fitsIn(Rectangle((0, 0), (6, 6))))
    assertEquals(false, Rectangle((0, 0), (1, 3)).fitsIn(Rectangle((0, 0), (6, 2))))
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

  def testLineToPoints {
    var line = new Line(0, 0, 10, 10)
    val points = line.toPoints(10)
    assertEquals(10, points.size)
    assertEquals(Point(0, 0), points(0))
    assertEquals(Point(10, 10), points(9))
    assertEquals(Point(5, 5), points(5))
  }
}
