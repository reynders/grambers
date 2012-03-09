package grambers
import junit.framework._
import Assert._
import java.lang.Math._

class ThingTest extends TestCase {
  
  def testNothing = {
    assertTrue(1==1)
  }
/*    
  def testCreateImageRoundThing = {
    val thing = new ImageRoundThing(Point(0, 0), 25, "resources/gfx/ball_50x50.gif")
    assertTrue(1==1)
  }
    
  def testImageRotation = {
    val thing = new ImageRoundThing(Point(0, 0), 25, "resources/gfx/ball_50x50.gif")
    assertEquals(0, thing.getRotatedImageNumberBasedOnDirection)
    thing.direction = 180
    assertEquals((thing.ROTATED_IMAGE_COUNT / 2), thing.getRotatedImageNumberBasedOnDirection)
    thing.direction = 359
    assertEquals((thing.ROTATED_IMAGE_COUNT-1), thing.getRotatedImageNumberBasedOnDirection)
  }
*/
}
