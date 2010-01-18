package grambers
import junit.framework._
import Assert._
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.Expectations
import org.jmock.Expectations._

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
      box_1.location = (2, 2)
      val box_2 = new grambers.Box(2, 2)
      box_2.location = (4, 2)
      val box_0_mock = (context.mock(classOf[grambers.Box])).asInstanceOf[grambers.Box]

      universe.things += box_0_mock      
      universe.things += box_1
      universe.things += box_2

      context.checking(new Expectations {  
        allowing(box_0_mock).location;  will(returnValue((0.0, 0.0)))
        allowing(box_0_mock).w; will(returnValue(1))
        allowing(box_0_mock).h; will(returnValue(1))

        exactly(2).of(box_0_mock).collidesWith(`with`(any(classOf[Thing])))
          will(returnValue(false))
      }) 
      
      universe.collide(universe.things)      
      context.assertIsSatisfied
    }
    
    def testCalculateCollisionAngle {
      val universe = new Universe(100, 100)
      
      val circle_1 = new grambers.RoundThing(1)
      circle_1.location = (0, 0)
      circle_1.direction = 45.0
      val circle_2 = new grambers.RoundThing(1)
      circle_2.location = (1, 1)
      circle_1.direction = -45.0

      assertEquals(45.0, universe.calculateCollisionAngle(circle_1, circle_2))
    }
    
    def testScalaCollectionPerformance {
      val arrayList = new java.util.ArrayList[Int](1)
      val scalaList = List(1)
      
      val t1 = System.currentTimeMillis()
      for (i <- 0 to 10000) {
        val number = i
        arrayList.add(i)
      }
      val t2 = System.currentTimeMillis()

      val t3 = System.currentTimeMillis()
      for (i <- 0 to 10000) {
        val number = i
        i::scalaList
      }
      val t4 = System.currentTimeMillis()
      println("arrayList: " + (t2-t1) + "ms, scalaList: " + (t4-t3) + "ms")
    }      
      
}
