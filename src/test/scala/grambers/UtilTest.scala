package grambers

import junit.framework._
import Assert._

class UtilTest extends TestCase {
  def testPointArrayStrToPointArray {
    val pointArrayStr = "0,0 -1,-1 2,2"
    val pointArray = Util.pointArrayStrToPointArray(pointArrayStr)

    assertEquals(3, pointArray.size)
    assertEquals(Point(0,0), pointArray(0))
    assertEquals(Point(-1,-1), pointArray(1))
    assertEquals(Point(2,2), pointArray(2))

    assertEquals(0, Util.pointArrayStrToPointArray("").size)
  }
}