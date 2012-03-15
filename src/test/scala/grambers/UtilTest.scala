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

  def testStrPointToPoint {
  	assertEquals(Point(0,0), Util.strPointToPoint("0,0"))
  	assertEquals(Point(-999,0), Util.strPointToPoint("-999,0"))
  	assertEquals(Point(-1,-1), Util.strPointToPoint("-1,-1"))
  }

  def testParseInt(str : String, default : Int) {
    assertEquals(1, Util.parseInt("1", 0))
    assertEquals(-1, Util.parseInt("-1", 0))
    assertEquals(42, Util.parseInt("", 42))
  }
}