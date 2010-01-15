package grambers

import scala.collection.mutable._

abstract class Shape {
  
  //def intersects(shape : Shape) : Boolean
}

class Line(val startX : Double, val startY : Double, val endX : Double, val endY : Double) extends Shape {
 
  lazy val asVector = new Vector(endX - startX, endY - startY)
  lazy val asUnitVector = asVector.unitVector
  lazy val length = asVector.length
  
  override def equals(line : Any) = line match {    
    case that : Line => 
      (this.startX == that.startX && this.startY == that.startY &&
      this.endX == that.endX && this.endY == that.endY) ||
      (this.startX == that.endX && this.startY == that.endY &&
      this.endX == that.startX && this.endY == that.startY)
    case _ => 
      false      
  }
  
  def distanceFrom(shape : Shape) : Double = shape match {
    case circle : Circle => {
      val lineToCircleVector = new Vector(circle.x - startX, circle.y - startY)
      val dotProduct = lineToCircleVector.dot(this.asUnitVector)

      if (dotProduct > 0) {
        if (dotProduct <= this.length) {
          val circleProjectionOnLine = projectionOn(circle)
          val normalVector = new Vector(circle.x - startX, circle.y - startY) - circleProjectionOnLine
          return normalVector.length
        }
        else {
          return new Vector(circle.x - endX, circle.y - endY).length
        }
      }
      else {
        return lineToCircleVector.length
      }
    }
    case _ => {
      println("Do not know how to calculate distance between a line and " + shape)
      return 0.0
    }
  }
  
  
  def projectionOn(shape : Shape) : Vector = shape match {
      case circle : Circle => {
println("Dot product: " +new Vector(circle.x - startX, circle.y - startY).dot(this.asUnitVector))          
        return new Vector(circle.x - startX, circle.y - startY).projectionOn(this.asUnitVector) 
      }
      case _ => {
        println("Line does not know how to get normal between itself and " + shape)
        return new Vector(0, 0)
      }    
  }
  
  override def toString : String = {
    return "(" + startX + "," + startY + "-" + endX + "," + endY + ")"
  }
}

class Circle(val x: Double, val y : Double, val r : Double) extends Shape {
  
    override def toString : String = {
      return "Circle(" + x + "," + y + ") : " + r + "r)"
    }
}

/** 
  Rectangle is specified as w, h and center point (x, y) instead of left upper 
  and right lower corner so that rectangle can be at an angle if needed
  */
class Rectangle(val x : Double, val y : Double, val w : Double, val h : Double) extends Shape {

  lazy val asLines = convertToLines
  
  def convertToLines : Buffer[Line] = {
    val lines = new ArrayBuffer[Line]()
    
    lines += new Line(x-w/2, y-h/2, x+w/2, y-h/2)
    lines += new Line(x+w/2, y-h/2, x+w/2, y+h/2)
    lines += new Line(x+w/2, y+h/2, x-w/2, y+h/2)
    lines += new Line(x-w/2, y+h/2, x-w/2, y-h/2)    
    
    return lines
  }
  
  def normal(circle : Circle): Vector = {

asLines.foreach {line => 
      val lineToCircleVector = new Vector(circle.x - line.startX, circle.y - line.startY)
      val lineToCircleProjection = lineToCircleVector.projectionOn(line.asVector)     
      println(line + " vector towards " + circle + " is " + lineToCircleVector + ", length " + lineToCircleVector.length +
              ", projection is " + lineToCircleProjection + ", length " + lineToCircleProjection.length)
}

    return new Vector(0, 0)
  }
  
  /*
  def closestCorner(pX : Double, pY : Double) : (Double, Double) = {
    val luX = (x- w/2) val luY = (y - h/2)
    if (pX - (x-w/2)) >
  }*/
  

  
  def overlaps(otherRectangle : Rectangle) : Boolean = {
      val o_left = otherRectangle.x - (otherRectangle.w/2)
      val left = x - (w/2)
      val o_right = o_left + otherRectangle.w
      val right = left + w
      val o_top = otherRectangle.y + (otherRectangle.h/2)
      val top = y + (h/2)
      val o_bottom = o_top - otherRectangle.h
      val bottom = top - h

      if ((bottom > o_top) || (top < o_bottom) ||
          (right < o_left) ||(left > o_right))
        return false
      else
        return true
    }
    
    override def toString : String = {
      return "Rectangle(" + x + "," + y + ") : " + w + "w, " + h + "h)"
    }
    
}


