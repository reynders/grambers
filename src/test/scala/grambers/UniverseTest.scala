package grambers
import junit.framework._
import Assert._
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations
import org.jmock.Expectations._
import net.iharder.Base64

class UniverseTest extends TestCase {

    val context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    def testToRadians() {
      import java.lang.Math
      assertEquals(0.0, Math.toRadians(0))
      assertEquals(Math.round(Math.PI), Math.round(Math.toRadians(180)))
    }
       
    def testCollideThings {
      val universe = new Universe(100, 100)
      val box_1 = new grambers.Box(2, 2)      
      box_1.center = new Point(1, 1)
      val circle_1 = new grambers.RoundThing(2)
      circle_1.center = new Point(0, 0)
      val box_0_mock = (context.mock(classOf[grambers.Box])).asInstanceOf[grambers.Box]

      universe.addThing(box_0_mock)      
      universe.addThing(box_1)
      universe.addThing(circle_1)

      context.checking(new Expectations {  
        allowing(box_0_mock).center;  will(returnValue(new Point(0.0, 0.0)))
        allowing(box_0_mock).w; will(returnValue(1))
        allowing(box_0_mock).h; will(returnValue(1))

        exactly(1).of(box_0_mock).collidesWith(`with`(any(classOf[Thing])))
          will(returnValue(false))
          
        exactly(0).of(box_0_mock).collidesWith(`with`(any(classOf[StaticThing])))
          will(returnValue(false))
      }) 
      
      universe.collide(universe.movingThings, universe.staticThings)      
      context.assertIsSatisfied
    }
    
    def testCalculateCollisionAngle {
      val universe = new Universe(100, 100)
      
      val circle_1 = new grambers.RoundThing(1)
      circle_1.center = new Point(0, 0)
      circle_1.direction = 45.0
      val circle_2 = new grambers.RoundThing(1)
      circle_2.center = new Point(1, 1)
      circle_1.direction = -45.0

      assertEquals(45.0, universe.calculateCollisionAngle(circle_1, circle_2))
    }
    
    def testBase64Decode {
      val byteArray : Array[Byte] = Base64.decode("H4sIAAAAAAAAAO3NoREAMAgEsLedAfafE4+s6l0jolNJiif18tt/Fj8AAMC9ARtYg28AEAAA")
      assertTrue(byteArray(0) != 0)
    }
}
