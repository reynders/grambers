package grambers
import junit.framework._
import Assert._
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations
import org.jmock.Expectations._
import net.iharder.Base64

class UniverseTest extends TestCase {

    def testToRadians() {
      import java.lang.Math
      assertEquals(0.0, Math.toRadians(0))
      assertEquals(Math.round(Math.PI), Math.round(Math.toRadians(180)))
    }
}
