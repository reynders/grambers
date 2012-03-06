package grambers

import junit.framework._
import Assert._

class ObserverTest extends TestCase {
  val WINDOW_W = 600
  val WINDOW_H = 300
  
  def testObserverViewTranslations {
    val universe = new Universe("resources/maps/testmap_40x20.tmx", true)
    val observer = new Observer(WINDOW_W, WINDOW_H, universe, new grambers.CircleMovingThing(Point(1, -1), 2))
    observer.w = 100
    observer.h = 50
    assertEquals(-590.0, observer.xViewTranslation)
    assertEquals(-295.0, observer.yViewTranslation)
    observer.position = Point(101, 51)
    assertEquals(-51.0, observer.xViewTranslation)
    assertEquals(-26.0, observer.yViewTranslation)
  }
  
  
  def testObserverViewTranslations2 {
    val universe = new Universe("resources/maps/testmap_40x20.tmx", true)
    val observer = new Observer(WINDOW_W, WINDOW_H, universe, new grambers.CircleMovingThing(Point(1, -1), 2))
    observer.w = 100
    observer.h = 50
    observer.position = Point(101, 51)
    assertEquals(-51.0, observer.xViewTranslation)
    assertEquals(-26.0, observer.yViewTranslation)
  }
}
