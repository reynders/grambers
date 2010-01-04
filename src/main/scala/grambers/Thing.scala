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
      val o_left = otherThing.location._1 - (otherThing.w/2)
      val left = location._1 - (w/2)
      val o_right = o_left + otherThing.w
      val right = left + w
      val o_top = otherThing.location._2 + (otherThing.h/2)
      val top = location._2 + (h/2)
      val o_bottom = o_top - otherThing.h
      val bottom = top - h

      if ((bottom > o_top) || (top < o_bottom) ||
          (right < o_left) ||(left > o_right))
        return false

      return true
    }
    
    def accelerate(amount : int) {
        speed += amount
    }

    def distanceFrom(otherThing : Thing) : Double = {
      val xDiff = Math.abs(otherThing.location._1 - location._1)
      val yDiff = Math.abs(otherThing.location._2 - location._2)
      
      return Math.sqrt((xDiff*xDiff) + (yDiff*yDiff)) 
    }
    
    def draw(g2 : Graphics2D );
        
    override def toString : String = {
        "(" + location._1 + "," + location._2 + "):" + speed + ":" + direction
    }
}

class Circle(val radius:Int) extends Thing(radius*2, radius*2) {
  
  var color = java.awt.Color.yellow
  
  override def collidesWith(otherThing : Thing) : Boolean = {
    otherThing match {
      case otherCircle : Circle => collide(otherCircle)
      case _ => return super.collidesWith(otherThing)
    }
  }
  
  def collide(otherCircle : Circle) : Boolean = {
    if ((radius + otherCircle.radius) < distanceFrom(otherCircle))
      return false
    else 
      return true
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