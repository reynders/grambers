package grambers
import junit.framework._
import Assert._

class Test extends TestCase {

    def testAddOne() {
        var main = grambers.Main
        assertEquals(2, main.addOne(1));
        assertEquals(0, main.addOne(-1));   
    }
    
}
