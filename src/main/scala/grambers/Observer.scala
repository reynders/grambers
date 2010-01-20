package grambers

import javax.swing._
import java.awt._
import java.awt.image._

// Philosophy says that observer makes immaterial things material, thus the class that
// does the rendering was named as Observer :) 
class Observer (val universe : Universe) {

    val FPS = 20;
    val screenBuffer = new BufferedImage(universe.WIDTH, universe.HEIGHT, BufferedImage.TYPE_INT_RGB);
    val sbg = screenBuffer.getGraphics().asInstanceOf[Graphics2D];
    val random = new Random()
    
    object WindowToWorld extends JFrame {
        object ViewPanel extends JPanel {
           override def paint (g : Graphics) {
                super.paint(g)
                g match {
                    case g2: Graphics2D => drawUniverse(g2)
                    case _ => throw new ClassCastException
                }
           }
        }

        def drawUniverse(g2 : Graphics2D) {
            clearScreenBuffer()
            sbg.setColor(Color.black);
            for (thing <- universe.things) {
              thing.draw(sbg)      
            }

            g2.drawImage(screenBuffer, 0, 0, null);
        }

        def clearScreenBuffer() {
            sbg.setColor(Color.green);
            sbg.fillRect(0, 0, universe.WIDTH, universe.HEIGHT);
        }
        
        def fpsToMs(fps:int) : int = {
            return (1000 / fps)
        }
        
        def start() {
            add(ViewPanel)
            pack()
            setSize(new Dimension(universe.WIDTH, universe.HEIGHT));
            show()
            while (true) {
                repaint()
                universe.advanceTime(1)
                Thread.sleep(fpsToMs(FPS))
            }
        }
    }
    
    def observe() {
      WindowToWorld.start                  
    }
}

