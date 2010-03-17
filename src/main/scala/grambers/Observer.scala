package grambers

import java.lang.System._
import javax.swing._
import java.awt._
import java.awt.image._

class Camera {
  def move(observer : Observer) = {}
}

class Observer (val universe : Universe, var thingInFocus : Thing) {
    
  var position : Point = Point(universe.WIDTH/2, universe.HEIGHT/2)
  var thingInControl = thingInFocus
  
  var w : Int = universe.WIDTH
  var h : Int = universe.HEIGHT
  val random = new Random()
  var fps = 0
  
var alphaFix : Boolean = false
  
  var camera : Camera = new Camera {
                          override def move(observer : Observer) = {
                            observer.position = thingInFocus.center
                          }
                        }
    
  def xViewTranslation = -1 * (position.x - w/2)
  def yViewTranslation = -1 * (position.y - h/2)

  def positionConsideringAlpha(thing : Thing) : Point = {
    val alpha : Double = if (alphaFix) universe.msSinceLastWorldUpdate.toDouble/1000 else 0.0
    val xPoint = thing.center.x + (thing.xSpeed * alpha)
    val yPoint = thing.center.y + (thing.ySpeed * alpha)
/*
if (universe.msSinceLastWorldUpdate % 10 == 0 )
  println("msSinceLastWorldUpdate: " + universe.msSinceLastWorldUpdate + ", " +
          "thing.center.x: " + thing.center.x + ", " +
          "thing.xSpeed: " + thing.xSpeed + ", " +
          "alpha: " + alpha + ", " +
          "xPoint: " + xPoint)
*/          
    return Point(xPoint, yPoint)
  }
    
  object WindowToWorld extends JFrame {
      
    initGraphics        
    
    import java.awt.event._
    object ObserverKeyListener extends KeyAdapter {
      override def keyPressed(e : KeyEvent) = {
        val c = e.getKeyCode();
        c match {
          case KeyEvent.VK_LEFT => thingInControl.turn(-1)
          case KeyEvent.VK_RIGHT => thingInControl.turn(1)
          case KeyEvent.VK_DOWN => thingInControl.accelerate(-1)
          case KeyEvent.VK_UP => thingInControl.accelerate(1)
          case KeyEvent.VK_A => alphaFix = !alphaFix; println("Setting alphaFix to " + alphaFix); 
          case _ => println("Caught key event " + c)
        }
        
        e.consume();
      }
    }
    
    object ViewPanel extends JPanel {
  
      override def paintComponent (g : Graphics) {
        //super.paintComponent(g)
        g match {
          case g2: Graphics2D => drawUniverse(g2)
          case _ => throw new ClassCastException
        }
      }

var previousX = 0
      
      def drawUniverse(g2 : Graphics2D) {
        val at = g2.getTransform();
        //g2d.transform(...);
        //g2.setColor(Color.black);
        g2.translate(xViewTranslation, yViewTranslation)
        universe.staticThings.foreach(thing => thing.draw(g2, thing.center))
        universe.movingThings.foreach(thing => { 
              thing.draw(g2, positionConsideringAlpha(thing))
if (positionConsideringAlpha(thing).x.toInt != previousX) {
  previousX = positionConsideringAlpha(thing).x.toInt
  println("New X: " + previousX);
}
        })

        g2.setTransform(at);

        fps += 1
      }

      def clearScreenBuffer(g2 : Graphics2D) {
        g2.setColor(Color.lightGray)
        g2.fillRect(0, 0, universe.WIDTH, universe.HEIGHT);
      }        
    }
      
    def initGraphics {
      
      addKeyListener(ObserverKeyListener)
      getRootPane.setDoubleBuffered(true)                
      add(ViewPanel)
      pack()
      setSize(new Dimension(w, h));
      setVisible(true)
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
  
  def observe() {    
    WindowToWorld.repaint() 
    camera.move(this)
    showStatistics
  } 
}

