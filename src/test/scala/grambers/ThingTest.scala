package grambers
import junit.framework._
import Assert._

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
    
    def testToString() {
        val thing = new grambers.Box(1, 1)
        thing.location = (3, 5)
        thing.speed = 10
        thing.direction = 180
        assertEquals("(3.0,5.0):10:180", thing.toString)
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
      box_2.location = (4, 4)
      assertFalse(box_1.collidesWith(box_2))
    }
    
    def testCollidesWithBoxesThatOverlap() {
      val box_1 = new grambers.Box(4, 4)      
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (3, 3)
      println("Failing test")
      assertTrue(box_1.collidesWith(box_2))
    }
}
