package grambers

import junit.framework._
import Assert._

class ObserverTest extends TestCase {
  
  def testObserverViewTranslations {
    val universe = new Universe(200, 100)
    val observer = new Observer(universe)
    observer.w = 100
    observer.h = 50
    assertEquals(-50.0, observer.xViewTranslation)
    assertEquals(-25.0, observer.yViewTranslation)
    observer.position = Point(101, 51)
    assertEquals(-51.0, observer.xViewTranslation)
    assertEquals(-26.0, observer.yViewTranslation)
  }
}
