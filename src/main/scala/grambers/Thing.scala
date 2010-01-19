package grambers

import java.awt._
import java.lang.Math._

abstract class Thing (val w:Int, val h:Int) {
    var location: (double, double) = (0, 0)
    var speed : Double = 0.0
    var direction : Double = 0
    var mass : Double = 1.0
    var doYourThing : ((Thing) => Unit) = (thing) => {}

    def turn(degrees:int) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }

   def collidesWith(thing : Thing) : Boolean = {
     this.shape.collidesWith(this.shape, thing.shape)
   }
  
    def accelerate(amount : int) {
        speed += amount
    }

    def xSpeed : Double = {
      return speed * cos(toRadians(direction))
    }
    
    def ySpeed : Double = {
      return speed * sin(toRadians(direction))
    }


    def setSpeedAndDirection(xSpeed : Double, ySpeed : Double) {
      speed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed)
      direction = toDegrees(atan2(ySpeed, xSpeed))
      
      if (ySpeed < 0) direction += 360
    }
    
    def setSpeedAndDirection(vector : Vector) {
      setSpeedAndDirection(vector.i, vector.j)
    }
 
    def distanceFrom(otherThing : Thing) : Double = {
      val xDiff = Math.abs(otherThing.location._1 - location._1)
      val yDiff = Math.abs(otherThing.location._2 - location._2)
      
      return Math.sqrt((xDiff*xDiff) + (yDiff*yDiff)) 
    }
     
    def shape : Shape
    
    def draw(g2 : Graphics2D )
        
    override def toString : String = {
        "(" + location._1 + "," + location._2 + "):" + speed + ":" + direction
    }
}

class RoundThing(val radius:Int) extends Thing(radius*2, radius*2) {
  
  var color = java.awt.Color.yellow
   
  def shape : Shape = {
    new Circle(location._1, location._2, radius)
  }
  
  def draw(g2 : Graphics2D) {
    import java.awt.geom._
    val shape = new Ellipse2D.Double(location._1, location._2, radius*2, radius*2)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(shape)
    g2.setPaint(originalPaintColor)
  }
  
  override def toString : String = {
    return "RoundThing" + super.toString
  }
}

class Box(w:Int, h:Int) extends Thing(w, h) {
  var color = java.awt.Color.black

  def shape : Shape = {
    new Rectangle(location._1, location._2, w, h)
  }
  
  def draw(g2: Graphics2D) {
    import java.awt.geom._
    val shape = new Rectangle2D.Double(location._1, location._2, w, h)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(shape)
    g2.setPaint(originalPaintColor)
  }
  
  override def toString : String = {
    return "Box" + super.toString
  }
}