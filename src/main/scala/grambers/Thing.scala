package grambers

import java.awt._

abstract class Thing (val w:Int, val h:Int) {
    var location: (double, double) = (0, 0)
    var speed : int = 0
    var direction : int = 0
    var doYourThing : ((Thing) => Unit) = (thing) => {}
    
    def turn(degrees:int) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }

    def collidesWith(otherThing : Thing) : Boolean = {
      println("Otherthing w = " + otherThing.w + " and x is " + otherThing.location._1)
      val o_left = otherThing.location._1 - (otherThing.w/2)
      val left = location._1 - (w/2)
      val o_right = o_left + otherThing.w
      val right = left + w
      val o_top = otherThing.location._2 + (otherThing.h/2)
      val top = location._2 + (h/2)
      val o_bottom = o_top - otherThing.h
      val bottom = top - h

      println("ol " + o_left + " or " + o_right + " ot " + o_top + " ob " + o_bottom)
      println("l " + left + " r " + right + " t " + top + " b " + bottom)
      if (bottom > o_top) return false
      if (top < o_bottom) return false
      if (right < o_left) return false
      if (left > o_right) return false
      
      return true
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
  
  def collide(otherThing : Thing) : Boolean = {
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