package grambers

import java.awt._

abstract class Thing {
    var location: (int, int) = (0, 0)
    var speed : int = 0
    var direction : int = 0
    
    def turn(degrees:int) {
        direction += degrees
    }
    
    def accelerate(amount : int) {
        speed += amount
    }
    
    def draw(g2 : Graphics2D );
    
    override def toString : String = {
        location + ":" + speed + ":" + direction
    }
}

class Circle(radius:int) extends Thing {
  
  def draw(g2 : Graphics2D) {
    import java.awt.geom._
    g2.draw(new Ellipse2D.Double(location._1, location._2, radius*2, radius*2))
  }
}

class Box extends Thing {
  def draw(g2: Graphics2D) {
  }
}