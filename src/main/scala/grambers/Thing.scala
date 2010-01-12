package grambers

import java.awt._
import java.lang.Math._

abstract class Thing (val w:Int, val h:Int) {
    var location: (double, double) = (0, 0)
    var speed : Double = 0.0
    var direction : Double = 0
    var doYourThing : ((Thing) => Unit) = (thing) => {}

    // If the shape can change override this to return current enclosing box 
    def enclosingRectangle : Rectangle = {
      return new Rectangle(w, h, location._1, location._2)
    }    

    def turn(degrees:int) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }

    def collidesWith(otherThing : Thing) : Boolean = {
      return enclosingRectangle.overlaps(otherThing.enclosingRectangle)
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
//println("Speed: " + speed + ", dir: " + direction)
    }

    
    def setSpeedAndDirection(vector : Vector) {
      setSpeedAndDirection(vector.i, vector.j)
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
      case otherCircle : Circle => collidesWith(otherCircle)
      case box : Box => collidesWith(box)
      case _ => return super.collidesWith(otherThing)
    }
  }
  
  def collidesWith(otherCircle : Circle) : Boolean = {
    if ((radius + otherCircle.radius) < distanceFrom(otherCircle))
      return false
    else 
      return true
  }
  
  def collidesWith(box : Box) : Boolean = {
    if (super.collidesWith(box)) {
println("Cirle enclosing box collides with box enclosing box, checking for collision")
       
      return true
    }
    
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
  var color = java.awt.Color.black

  
  override def collidesWith(otherThing : Thing) : Boolean = {
    otherThing match {
      case circle : Circle => return circle.collidesWith(this)
      case _ => return super.collidesWith(otherThing)
    }  
        
    // See http://www.tonypa.pri.ee/vectors/tut07.html
    // http://vband3d.tripod.com/visualbasic/tut_mixedcollisions.htm
    // http://www.2dgamecreators.com/tutorials/gameprogramming/collision/T1%20Collision2.html#mozTocId39150
      return false
  }

  def draw(g2: Graphics2D) {
    import java.awt.geom._
    val shape = new Rectangle2D.Double(location._1, location._2, w, h)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(shape)
    g2.setPaint(originalPaintColor)
  }
}