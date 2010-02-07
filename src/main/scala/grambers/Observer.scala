package grambers

import java.lang.System._
import javax.swing._
import java.awt._
import java.awt.image._

class Camera {
  def move(observer : Observer) = {}
}

class Observer (val universe : Universe) {
    
  var position : Point = Point(universe.WIDTH/2, universe.HEIGHT/2)
  var w : Int = universe.WIDTH
  var h : Int = universe.HEIGHT
  val random = new Random()
  var fps = 0
  var camera : Camera = new Camera
    
  def xViewTranslation = -1 * (position.x - w/2)
  def yViewTranslation = -1 * (position.y - h/2)
 
  object WindowToWorld extends JFrame {
      
    initGraphics        
      
    object ViewPanel extends JPanel {
  
      override def paintComponent (g : Graphics) {
        //super.paintComponent(g)
        g match {
          case g2: Graphics2D => drawUniverse(g2)
          case _ => throw new ClassCastException
        }
      }

      def drawUniverse(g2 : Graphics2D) {
        val at = g2.getTransform();
        //g2d.transform(...);
        //g2.setColor(Color.black);
        g2.translate(xViewTranslation, yViewTranslation)
        universe.staticThings.foreach(_.draw(g2))
        universe.movingThings.foreach(_.draw(g2))

        g2.setTransform(at);

        fps += 1
      }

      def clearScreenBuffer(g2 : Graphics2D) {
        g2.setColor(Color.lightGray)
        g2.fillRect(0, 0, universe.WIDTH, universe.HEIGHT);
      }        
    }
      
    def initGraphics {
        
      getRootPane.setDoubleBuffered(true)                
      add(ViewPanel)
      pack()
      setSize(new Dimension(w, h));
      show()
    }
  }

  var measurementStartTime = currentTimeMillis      
  val measurementSamplePeriodMs = 5000
        
  def showStatistics {
    if (currentTimeMillis - measurementStartTime > measurementSamplePeriodMs) {
      println((fps/(measurementSamplePeriodMs/1000)) + " FPS, " + (universe.worldUpdates/(measurementSamplePeriodMs/1000)) + " world updates")
      fps = 0; universe.worldUpdates = 0; measurementStartTime = currentTimeMillis ; 
    }
  }
  
  var xDir = 1
  var yDir = 1
  var lastUpdate = currentTimeMillis
  
  // DEMO, replace with real implementation
  def move {
    if (currentTimeMillis - lastUpdate > 1000) {
      if (position.x + (w/2) > universe.WIDTH)
        xDir *= -1
      if (position.y + (h/2) > universe.HEIGHT)
        yDir *= -1   
      
      println("Setting position from " + position + " to .x " + Point(position.x + xDir, position.y + yDir) )
      position = Point(position.x + xDir, position.y + yDir)  
      lastUpdate = currentTimeMillis
    }
  }
  
  def observe {    
      WindowToWorld.repaint()
      camera.move(this)
      showStatistics
  } 
}

