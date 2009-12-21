package grambers

import javax.swing._
import java.awt._
import java.awt.image._
import java.util.Random

// Philosophy says that observer makes immaterial things material, thus the class that
// does the rendering was named as Observer :) 
class Observer (val WIDTH : int, val HEIGHT : int) {

    val FPS = 20;
    val screenBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    val random = new Random()
    
    object WindowToWorld extends JFrame {
        object ViewPanel extends JPanel {
           override def paint (g : Graphics) {
                super.paint(g)
                g match {
                    case g2: Graphics2D => draw(g2)
                    case _ => throw new ClassCastException
                }
           }
        }

        def draw(g2:Graphics2D) {
            val sbg = screenBuffer.getGraphics()
            sbg.setColor(Color.green);
            sbg.fillRect(0, 0, WIDTH, HEIGHT);
            sbg.setColor(Color.black);
            sbg.drawString("I REALLY AM ALIVE", random.nextInt(WIDTH), random.nextInt(HEIGHT))      
            g2.drawImage(screenBuffer, 0, 0, null); 
        }

        def fpsToMs(fps:int) : int = {
            return (1000 / fps)
        }
        
        def start() {
            add(ViewPanel)
            pack()
            setSize(new Dimension(WIDTH, HEIGHT));
            show()
            while (true) {
                repaint()
                Thread.sleep(fpsToMs(FPS))
            }
        }
    }
    
    def observe {
      WindowToWorld.start                  
    }
}

