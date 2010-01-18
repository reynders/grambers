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
  
  def distanceFrom(pointX : Double, pointY : Double) : Double = {
    val lineToPointVector = new Vector(pointX - startX, pointY - startY)
    val dotProduct = lineToPointVector.dot(this.asUnitVector)

    if (dotProduct > 0) {
      if (dotProduct <= this.length) {
        val pointProjectionOnLine = new Vector(pointX - startX, pointY - startY).projectionOn(this.asUnitVector)
        val normalVector = new Vector(pointX - startX, pointY - startY) - pointProjectionOnLine
        return normalVector.length
      }
      else {
        return new Vector(pointX - endX, pointY - endY).length
      }
    }
    else {
      return lineToPointVector.length
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
  lazy val minX = x - (w/2)
  lazy val minY = y - (h/2)
  lazy val maxX = x + (w/2)
  lazy val maxY = y + (h/2)
  
  def convertToLines : Buffer[Line] = {
    val lines = new ArrayBuffer[Line]()
    
    lines += new Line(minX, minY, maxX, minY)
    lines += new Line(maxX, minY, maxX, maxY)
    lines += new Line(maxX, maxY, minX, maxY)
    lines += new Line(minX, maxY, minX, minY)    
    
    return lines
  }
  
  def collidesWith (shape : Shape) : Boolean = shape match {
    case circle : Circle => {
      return collidesWith(circle)
    }
    case _ => return false  
  }
 
  def collidesWith(circle : Circle) : Boolean = {
    val distance = 
      if (circle.x < minX) {
        asLines(3).distanceFrom(circle.x, circle.y)
      } 
      else if (circle.x > maxX) {
        asLines(1).distanceFrom(circle.x, circle.y)
      }
      else if (circle.y < minY) { 
        asLines(0).distanceFrom(circle.x, circle.y)      
      }
      else if (circle.y > maxY) { 
        asLines(2).distanceFrom(circle.x, circle.y)
      }
      else {        
        println(circle + " is inside " + this + ", do something!")
        0.0
      }
    return circle.r >= distance
  }

  
  def overlaps(other : Rectangle) : Boolean = {
    return !((minY > other.maxY) || (maxY < other.minY) ||
             (maxX < other.minX) ||(minX > other.maxX))
  }
    
  override def toString : String = {
    return "Rectangle(" + x + "," + y + ") : " + w + "w, " + h + "h)"
  }  
}


