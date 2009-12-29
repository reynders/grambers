package grambers

import java.awt._

abstract class Thing (w:Int, h:Int) {
    var location: (double, double) = (0, 0)
    var speed : int = 0
    var direction : int = 0
    var doYourThing : ((Thing) => Unit) = (thing) => {}
    
    def turn(degrees:int) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }

    def collide(otherThing : Thing) : Boolean = {
      return false
    }
  
    def accelerate(amount : int) {
        speed += amount
    }

    
    def draw(g2 : Graphics2D );
        
    override def toString : String = {
        "(" + location._1 + "," + location._2 + "):" + speed + ":" + direction
    }
}

class Circle(radius:Int) extends Thing(radius*2, radius*2) {
  
  var color = java.awt.Color.yellow
  
  override def collide(otherThing : Thing) : Boolean = {
    otherThing match {
      case otherCircle: Circle => collide(otherCircle)
      case _ => return false
    }
  }
  
  def collide(otherCircle : Circle) : Boolean = {
    // if distance is < r1 + r2 we have a collision
    return false
    
  }
  
  def draw(g2 : Graphics2D) {
    import java.awt.geom._
    val shape = new Ellipse2D.Double(location._1, location._2, radius*2, radius*2)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(shape)
    g2.setPaint(originalPaintColor)
  }
}

class Box(w:Int, h:Int) extends Thing(w, h) {
  def draw(g2: Graphics2D) {
  }
}