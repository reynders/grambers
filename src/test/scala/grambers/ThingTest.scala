package grambers
import junit.framework._
import Assert._

class ThingTest extends TestCase {

    def testCircleLocationVal() {
        val location = (1.0, 2.0)
        val thing = new grambers.Circle(1, java.awt.Color.red)
        thing.location = location
        assertEquals(1.0, thing.location._1)
        assertEquals(2.0, thing.location._2)
    }    
    
    def testToString() {
        val thing = new grambers.Box()
        thing.location = (3, 5)
        thing.speed = 10
        thing.direction = 180
        assertEquals("(3.0,5.0):10:180", thing.toString)
    }
}
