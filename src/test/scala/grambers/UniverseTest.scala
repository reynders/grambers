package grambers
import junit.framework._
import Assert._

class UniverseTest extends TestCase {

    def testCircleLocationVal() {
        val location = (1, 2)
        val thing = new grambers.Circle()
        thing.location = location
        assertEquals(1, thing.location._1);
        assertEquals(2, thing.location._2);
    }    
}
