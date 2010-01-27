package grambers
import junit.framework._
import Assert._
import java.lang.Math._

class ThingTest extends TestCase {

    def testRoundThingLocationVal() {
        val thing = new grambers.RoundThing(1)
        thing.center = new Point(1.0, 2.0)
        assertEquals(1.0, thing.center.x)
        assertEquals(2.0, thing.center.y)
    }    
    
    def testTurn() {
      val thing = new grambers.RoundThing(1)
      thing.direction = 0
      thing.turn(1)
      assertEquals(1.0, thing.direction)
      thing.direction = 0
      thing.turn(361)
      assertEquals(1.0, thing.direction)
      thing.direction = 0
      thing.turn(-361)
      assertEquals(359.0, thing.direction)
    }

    def testXandYspeed() {
      val thing = new grambers.RoundThing(1)
      thing.direction = 45
      thing.speed = Math.sqrt(2.0)
      assertEquals(1.0, Math.floor(thing.ySpeed))
      assertEquals(1.0, Math.floor(thing.xSpeed))
      thing.speed = 1
      thing.direction = 90
      assertEquals(1.0, Math.floor(thing.ySpeed))
      assertEquals(0.0, Math.floor(thing.xSpeed))      
      
    }
    
    def testSetSpeedAndDirection() {
      val thing = new grambers.RoundThing(1)
      thing.direction = 0
      thing.speed = 0 //Math.sqrt(2.0)
      thing.setSpeedAndDirection(1, 1)
      assertEquals(45.0, thing.direction)
      assertEquals(Math.sqrt(2.0), thing.speed)

      thing.setSpeedAndDirection(-3, -3)
      assertEquals(225.0, thing.direction)
      assertEquals(Math.sqrt(18.0), thing.speed)
      
      thing.setSpeedAndDirection(0, 1)
      assertEquals(90.0, thing.direction)

      thing.setSpeedAndDirection(0, -1)
      assertEquals(270.0, thing.direction)

      thing.setSpeedAndDirection(1, 0)
      assertEquals(0.0, thing.direction)

      thing.setSpeedAndDirection(0, -1)
      assertEquals(270.0, thing.direction)

      thing.setSpeedAndDirection(-1, 0)
      assertEquals(180.0, thing.direction)
    }

    def testToString() {
        val thing = new grambers.Box(Point(3, 5), 1, 1)
        thing.speed = 10.0
        thing.direction = 170
        assertEquals("Box(3.0,5.0):(1.0w,1.0h):10.0p/s:170.0dg,1.0w,1.0h", thing.toString)
    }
    
    def testWidthAndHeigth() {
      val circle = new grambers.RoundThing(3)
      assertEquals(circle.w, 6.0)
      assertEquals(circle.h, 6.0)
      val box = new grambers.Box(2, 2)
      assertEquals(box.w, 2.0)
      assertEquals(box.h, 2.0)
    }
    
    def testCollidesWithBoxesThatDoNotCollide() {
      val box_1 = new grambers.Box(Point(2, 2), 2, 2)

      val box_2 = new grambers.Box(Point(5, 5), 2, 2)
      assertFalse(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithBoxesThatOverlap() {
      val box_1 = new grambers.Box(Point(2, 2), 4, 4)      
      val box_2 = new grambers.Box(Point(3, 3), 2, 2)
      assertTrue(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithAdjacentBoxes() {
      val box_1 = new grambers.Box(Point(2, 2), 2, 2)      
      box_1.direction = 0
      box_1.speed = 100
      val box_2 = new grambers.Box(Point(4, 2), 2, 2)             
      box_2.speed = 0
      assertTrue(box_1.collidesWith(box_2))
      assertTrue(box_2.speed == 0)
    }
    
    def testCollidesWithAdjacentCollidingRoundThings() {
      val circle_1 = new grambers.RoundThing(Point(0, 0), 1)
      val circle_2 = new grambers.RoundThing(Point(2, 0), 1)
      assertTrue(circle_1.collidesWith(circle_2))
    }

    def testCollidesWithOverlappingRoundThings() {
      val circle_1 = new grambers.RoundThing(Point(1, -1), 2)
      val circle_2 = new grambers.RoundThing(Point(0, 0), 1)
      assertTrue(circle_1.collidesWith(circle_2))
    }

    def testCollidesWithNonOverlappingRoundThings() {
      val circle_1 = new grambers.RoundThing(Point(0, -1), 1)
      val circle_2 = new grambers.RoundThing(Point(0, 2), 1)
      assertEquals(false, circle_1.collidesWith(circle_2))
    }
    
    def testDistanceFrom() {
      val circle_1 = new grambers.RoundThing(Point(0, 0), 1)
      val circle_2 = new grambers.RoundThing(Point(2, 0), 1)
      assertEquals(2.0, circle_1.distanceFrom(circle_2))
      circle_1.center = Point(2, -3)      
      assertEquals(3.0, circle_1.distanceFrom(circle_2))
    }
}
