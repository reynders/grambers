package grambers

import java.lang.System._
import javax.swing._
import java.awt._
import java.awt.image._

class Camera {
  def move(observer : Observer) = {}
}

class Observer (var w: Int, var h: Int, val universe : Universe, var thingInFocus : Thing) {
    
  var position : Point = Point(universe.WIDTH/2, universe.HEIGHT/2)
  var thingInControl = thingInFocus
  val random = new java.util.Random()
    
  var camera : Camera = new Camera {
                          override def move(observer : Observer) = {
                            observer.position = thingInFocus.center
                          }
                        }
    
  def xViewTranslation = -1 * (position.x - w/2)
  def yViewTranslation = -1 * (position.y - h/2)

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
          case KeyEvent.VK_D => Config.debugOn = !Config.debugOn; println("Setting debug to " + Config.debugOn); 
          case _ => println("Caught key event " + c)
        }
        
        e.consume();
      }
    }
    
    object WindowResizeListener extends ComponentAdapter {
      override def componentResized(e:ComponentEvent) {
        w = getWidth
        h = getHeight
        println("Window resized to (" + w + "," + h + ")")
      }
    }
    
    object ViewPanel extends JPanel {
  
      override def paintComponent (g : Graphics) {
        super.paintComponent(g)
        g match {
          case g2: Graphics2D => drawUniverse(g2)
          case _ => throw new ClassCastException
        }
      }
    
      def drawUniverse(g2 : Graphics2D) {
        val at = g2.getTransform();
        g2.translate(xViewTranslation, yViewTranslation)
        universe.map.drawBackground(g2, position, w, h)
        universe.staticThings.foreach(thing => thing.draw(g2, thing.center))
        universe.movingThings.foreach(thing => thing.draw(g2, thing.center))

        g2.setTransform(at);
        g2.dispose
        Config.fps += 1
      }
    }
      
    def initGraphics {      
      addKeyListener(ObserverKeyListener)
      addComponentListener(WindowResizeListener)
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //getRootPane.setDoubleBuffered(true)                
      add(ViewPanel)
      pack()
println("Setting size to (" + w + "," + h + ")")
      setSize(new Dimension(w, h));
println("Actual size is " + "(" + getWidth + "," + getHeight + ")")
      setVisible(true)
    }
  }

  def observe() {    
    WindowToWorld.repaint() 
    camera.move(this)
    //Thread.`yield`
  } 
}

