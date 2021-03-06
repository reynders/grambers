package grambers

import javax.swing._
import java.awt._
import Util.log

class Camera {
  def move(observer : Observer) = {}
}

class Observer (var w: Int, var h: Int, val universe : Universe, var thingInFocus : Thing) {

  var position : Point = Point(universe.WIDTH/2, universe.HEIGHT/2)
  var thingInControl = thingInFocus
  val random = new java.util.Random()

  var camera : Camera = new Camera {
                          override def move(observer : Observer) = {
                            var x = if ((thingInFocus.center.x - w/2) < 0) w/2
                                    else if ((thingInFocus.center.x + w/2) > universe.WIDTH) universe.WIDTH - w/2
                                    else thingInFocus.center.x
                            var y = if ((thingInFocus.center.y - h/2) < 0) h/2
                                    else if ((thingInFocus.center.y + h/2) > universe.HEIGHT) universe.HEIGHT - h/2
                                    else thingInFocus.center.y
                            observer.position.set(x, y)
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
          case KeyEvent.VK_D => Config.debugOn = !Config.debugOn; log.info("Setting debug to " + Config.debugOn);
          case _ => // Caught key event " + c)
        }

        e.consume();
      }
    }

    object WindowResizeListener extends ComponentAdapter {
      override def componentResized(e:ComponentEvent) {
        w = getWidth
        h = getHeight
        log.debug("Window resized to (" + w + "," + h + ")")
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

      var centerPoint = new Point(0, 0)

      def drawUniverse(g2 : Graphics2D) {

        if (Config.limitFps)
          Thread.sleep((1/Config.fpsLimit) * 1000)

        val bgImage = universe.map.getBackgroundImage(position, w, h)

        if (bgImage.image.getWidth != w)
          log.warn("bgImage width " + bgImage.image.getWidth + " is not window width " + w)

        g2.drawImage(bgImage.image, 0, 0, null)

        universe.staticThings.foreach{ thing =>
          thing.draw(g2, centerPoint.set(thing.center.x + xViewTranslation, thing.center.y + yViewTranslation))
        }

        universe.movingThings.foreach{ thing =>
          thing.draw(g2, centerPoint.set(thing.center.x + xViewTranslation, thing.center.y + yViewTranslation))
        }

        Config.fps += 1
      }
    }

    def initGraphics {
      addKeyListener(ObserverKeyListener)
      addComponentListener(WindowResizeListener)
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      add(ViewPanel)
      setSize(new Dimension(w, h));
      log.info("Window size is " + "(" + getWidth + "," + getHeight + ")")
      setVisible(true)
    }
  }

  def observe() {
    WindowToWorld.repaint()
    camera.move(this)
    Thread.`yield`
  }
}

