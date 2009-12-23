package grambers
import junit.framework._
import Assert._

class UniverseTest extends TestCase {

    def testToRadians() {
      import java.lang.Math
      assertEquals(0.0, Math.toRadians(0))
      assertEquals(Math.round(Math.PI), Math.round(Math.toRadians(180)))
    }
}
