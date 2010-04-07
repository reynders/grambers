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
    
  var camera : Camera = new Camera {
                          override def move(observer : Observer) = {
                            observer.position = thingInFocus.center
                          }
                        }
    
  def xViewTranslation = -1 * (position.x - w/2)
  def yViewTranslation = -1 * (position.y - h/2)

  def positionConsideringAlpha(thing : Thing) : Point = {
    var alpha : Double = if (Config.alphaFixOn) universe.msSinceLastWorldUpdate.toDouble/1000 else 0.0
    //println(Config.currentTimeMillis + " : Current alpha is " + alpha)
    val xPoint = thing.center.x + (thing.xSpeed * alpha)
    val yPoint = thing.center.y + (thing.ySpeed * alpha)
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
          case KeyEvent.VK_A => Config.alphaFixOn = !Config.alphaFixOn; println("Setting alphaFix to " + Config.alphaFixOn); 
          case _ => println("Caught key event " + c)
        }
        
        e.consume();
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
    
   /* 
    override def paintComponent(g : Graphics) {
      	val bf = getBufferStrategy();
        val g2d = bf.getDrawGraphics.asInstanceOf[Graphics2D]
        drawUniverse(g2d)
        g2d.dispose();
        //bf.show();
        //Toolkit.getDefaultToolkit().sync();
    }*/
    
      def drawUniverse(g2 : Graphics2D) {
        val at = g2.getTransform();
        g2.translate(xViewTranslation, yViewTranslation)
        universe.staticThings.foreach(thing => thing.draw(g2, thing.center))
        universe.movingThings.foreach(thing => { 
              thing.draw(g2, positionConsideringAlpha(thing))
        })

        g2.setTransform(at);
        g2.dispose
        Config.fps += 1
      }

      def clearScreenBuffer(g2 : Graphics2D) {
        g2.setColor(Color.lightGray)
        g2.fillRect(0, 0, universe.WIDTH, universe.HEIGHT);
      }        
    }
      
    def initGraphics {      
      addKeyListener(ObserverKeyListener)
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //getRootPane.setDoubleBuffered(true)                
      add(ViewPanel)
      pack()
      setSize(new Dimension(w+20, h+40));
      setVisible(true)
    }
  }

  def observe() {    
    WindowToWorld.repaint() 
    camera.move(this)
  } 
}

