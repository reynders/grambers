package grambers

import java.lang.System._
import javax.swing._
import java.awt._
import java.awt.image._

class Observer (val universe : Universe) {
    
    val random = new Random()
    var fps = 0
    
    object WindowToWorld extends JFrame {
      
      initGraphics        
      
      object ViewPanel extends JPanel {
  
        override def paintComponent (g : Graphics) {
          super.paintComponent(g)
          g match {
            case g2: Graphics2D => drawUniverse(g2)
            case _ => throw new ClassCastException
          }
        }

        def drawUniverse(g2 : Graphics2D) {
          //clearScreenBuffer(g2)
          g2.setColor(Color.black);

          universe.staticThings.foreach(_.draw(g2))
          universe.movingThings.foreach(_.draw(g2))
          
          fps += 1
        }

        def clearScreenBuffer(g2 : Graphics2D) {
          g2.setColor(Color.lightGray);
          g2.fillRect(0, 0, universe.WIDTH, universe.HEIGHT);
        }        
      }
      
      def initGraphics {
        
        getRootPane.setDoubleBuffered(true)        
        
        add(ViewPanel)
        pack()
        setSize(new Dimension(universe.WIDTH, universe.HEIGHT+25));
        show()
      }

        
      var measurementStartTime = currentTimeMillis      
      val measurementSamplePeriodMs = 5000
      var worldUpdates = 0
        
      def showStatistics {
        if (currentTimeMillis - measurementStartTime > 5000) {
          println((fps/(measurementSamplePeriodMs/1000)) + " FPS, " + (worldUpdates/(measurementSamplePeriodMs/1000)) + " world updates")
          fps = 0; worldUpdates = 0; measurementStartTime = currentTimeMillis ; 
        }
      }

       
      def start {          
        val worldUpdatesPerSecond = 50;
        val millisecondsBetweenWorldUpdates = 1000 / worldUpdatesPerSecond
        var nextWorldUpdateTime = currentTimeMillis
                  
        while (true) {
          val now = currentTimeMillis

          showStatistics            
            
          while (now > nextWorldUpdateTime) {             
            // We do not use REAL elapsed ms here because that would again be 
            // computer speed dependant
            universe.advanceTime(millisecondsBetweenWorldUpdates) 
            nextWorldUpdateTime += millisecondsBetweenWorldUpdates
            worldUpdates+=1           
          }
            
          repaint()
        }
      }
    }
    
    def observe() {
      WindowToWorld.start                  
    }
}

