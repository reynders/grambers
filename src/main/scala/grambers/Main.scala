package grambers

import javax.swing._
import java.awt._

object Main extends JFrame {

    object MyPanel extends JPanel {
        val myLabel = new JLabel("I'm alive")
        add(myLabel)
    }

    def addOne(number:Int): Int = {
        return number + 1;
    }

    def main(args:Array[String]) {
        println("I'm alive!")
        add(MyPanel)
        pack()
        setSize(new Dimension(400,300));
        show()
    }
}
