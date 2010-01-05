package grambers
import junit.framework._
import Assert._
import java.lang.Math._

class ThingTest extends TestCase {

    def testCircleLocationVal() {
        val location = (1.0, 2.0)
        val thing = new grambers.Circle(1)
        thing.location = location
        assertEquals(1.0, thing.location._1)
        assertEquals(2.0, thing.location._2)
    }    
    
    def testTurn() {
      val thing = new grambers.Circle(1)
      thing.direction = 0
      thing.turn(-1)
      assertEquals(359, thing.direction)
      thing.turn(-359)
      assertEquals(0, thing.direction)
      thing.turn(721)
      assertEquals(1, thing.direction)
    }

    def testXandYspeed() {
      val thing = new grambers.Circle(1)
      thing.direction = 45
      thing.speed = Math.sqrt(2.0)
      assertEquals(1.0, Math.floor(thing.ySpeed))
      assertEquals(1.0, Math.floor(thing.xSpeed))
      thing.speed = 1
      thing.direction = 90
      assertEquals(0.0, Math.floor(thing.ySpeed))
      assertEquals(1.0, Math.floor(thing.xSpeed))      
      
    }
    
    def testToString() {
        val thing = new grambers.Box(1, 1)
        thing.location = (3, 5)
        thing.speed = 10.0
        thing.direction = 180
        assertEquals("(3.0,5.0):10.0:180", thing.toString)
    }
    
    def testWidthAndHeigth() {
      val circle = new grambers.Circle(3)
      assertEquals(circle.w, 6)
      assertEquals(circle.h, 6)
      val box = new grambers.Box(2, 2)
      assertEquals(box.w, 2)
      assertEquals(box.h, 2)
    }
    
    def testCollidesWithBoxesThatDoNotCollide() {
      val box_1 = new grambers.Box(2, 2)
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (5, 5)
      assertFalse(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithBoxesThatOverlap() {
      val box_1 = new grambers.Box(4, 4)      
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (3, 3)
      assertTrue(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithAdjacentBoxes() {
      val box_1 = new grambers.Box(2, 2)      
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (4, 2)
      assertTrue(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithAdjacentCollidingCircles() {
      val circle_1 = new grambers.Circle(1)
      circle_1.location = (0, 0)

      val circle_2 = new grambers.Circle(1)
      circle_2.location = (2, 0)

      assertTrue(circle_1.collidesWith(circle_2))
    }

    def testCollidesWithOverlappingCircles() {
      val circle_1 = new grambers.Circle(2)
      circle_1.location = (1, -1)

      val circle_2 = new grambers.Circle(1)
      circle_2.location = (0, 0)

      assertTrue(circle_1.collidesWith(circle_2))
    }

    def testCollidesWithNonOverlappingCircles() {
      val circle_1 = new grambers.Circle(1)
      circle_1.location = (0, -1)

      val circle_2 = new grambers.Circle(1)
      circle_2.location = (0, 2)

      assertFalse(circle_1.collidesWith(circle_2))
    }
    
    def testDistanceFrom() {
      val circle_1 = new grambers.Circle(1)
      circle_1.location = (0, 0)

      val circle_2 = new grambers.Circle(1)
      circle_2.location = (2, 0)

      assertEquals(2.0, circle_1.distanceFrom(circle_2))
      circle_1.location = (2, -3)      
      assertEquals(3.0, circle_1.distanceFrom(circle_2))
    }
    
}
