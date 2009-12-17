package grambers

import javax.swing._
import java.awt._
import java.awt.image._
import java.util.Random

object Main extends JFrame {

    val MAX_X = 400
    val MAX_Y = 300
    val screenBuffer = new BufferedImage(MAX_X, MAX_Y, BufferedImage.TYPE_INT_RGB);
    val random = new Random()
    
    object MyPanel extends JPanel {
       val myLabel = new JLabel("I'm alive")
       add(myLabel)

       override def paint (g : Graphics) {
            super.paint(g)
            println("paint()");
            g match {
                case g2: Graphics2D => draw(g2)
                case _ => throw new ClassCastException
            }
       }
    }
    
    def draw(g2:Graphics2D) {
        println("draw()");
        screenBuffer.getGraphics().drawString("I REALLY AM ALIVE", random.nextInt(MAX_X), random.nextInt(MAX_Y))      
        g2.drawImage(screenBuffer, 0, 0, null); 
    }

    def addOne(number:Int): Int = {
        return number + 1;
    }

    def initGraphics() {
    }
    
    def main(args:Array[String]) {
        println("I'm alive!")
        add(MyPanel)
        pack()
        setSize(new Dimension(MAX_X, MAX_Y));
        show()
        while (true) {repaint()}
    }
}
