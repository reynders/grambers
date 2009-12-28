package grambers

import java.awt._

abstract class Thing {
    var location: (double, double) = (0, 0)
    var speed : int = 0
    var direction : int = 0
    var doYourThing : ((Thing) => Unit) = (thing) => {}
    
    def turn(degrees:int) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }
    
    def accelerate(amount : int) {
        speed += amount
    }

    def draw(g2 : Graphics2D );
        
    override def toString : String = {
        "(" + location._1 + "," + location._2 + "):" + speed + ":" + direction
    }
}

class Circle(val radius:int, val color : java.awt.Color) extends Thing {
    
  def draw(g2 : Graphics2D) {
    import java.awt.geom._
    val shape = new Ellipse2D.Double(location._1, location._2, radius*2, radius*2)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(shape)
    g2.setPaint(originalPaintColor)
  }
}

class Box extends Thing {
  def draw(g2: Graphics2D) {
  }
}