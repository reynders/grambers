package grambers

import scala.collection.mutable._

abstract class Shape(val center : Point) {
  
  def x : Double = center.x
  def y : Double = center.y
  
  def this(x : Double, y : Double) = this(new Point(x, y))
  
  def distanceFrom(shape : Shape) : Double = {
    new Vector(shape.x - x, shape.y - y).length
  }
}

object Shape {
  def collisionUnitVector(leftShape : Shape, rightShape : Shape) : Vector = (leftShape, rightShape) match {
    case (circle: Circle, line : Line) => line.shortestVectorTo(circle.x, circle.y).unitVector
    case (line : Line, circle: Circle) => line.shortestVectorTo(circle.x, circle.y).unitVector
    case (rectangle : Rectangle, circle : Circle) => rectangle.facingSide(circle.x, circle.y).shortestVectorTo(circle.x, circle.y).unitVector
    case (circle : Circle, rectangle : Rectangle) => rectangle.facingSide(circle.x, circle.y).shortestVectorTo(circle.x, circle.y).unitVector
    case (leftCircle : Circle, rightCircle : Circle) => new Vector((rightCircle.x - leftCircle.x), (rightCircle.y - leftCircle.y)).unitVector
    case _ => println("Do not know how to calculate collisionUnitVector between " + leftShape + " and " + rightShape)
              new Vector(1, 0)
  }
  
  def collidesWith(leftShape : Shape, rightShape : Shape) : Boolean = (leftShape, rightShape) match {
    case (rectangle : Rectangle, circle : Circle) => {
//println(circle.r + " vs " + rectangle.facingSide(circle.x, circle.y).distanceFrom(circle.x, circle.y) + " facing side " + rectangle.facingSide(circle.x, circle.y))     
      circle.r >= rectangle.facingSide(circle.x, circle.y).distanceFrom(circle.x, circle.y)
    }
    case (circle : Circle, rectangle : Rectangle) => circle.r >= rectangle.facingSide(circle.x, circle.y).distanceFrom(circle.x, circle.y) 
    case (leftCircle : Circle, rightCircle : Circle) => !((leftCircle.r + rightCircle.r) < leftCircle.distanceFrom(rightCircle))
    case (leftRectangle: Rectangle, rightRectangle : Rectangle) => leftRectangle.overlaps(rightRectangle)
    case _ => return false  
  }
  
}

class Point(val x : Double, val y : Double) {
}

class Line(val startX : Double, val startY : Double, val endX : Double, val endY : Double) extends Shape(startX, startY) {
 
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
 
  def shortestVectorTo(pointX : Double, pointY : Double) : Vector = {
    val lineToPointVector = new Vector(pointX - startX, pointY - startY)
    val dotProduct = lineToPointVector.dot(this.asUnitVector)

    if (dotProduct > 0) {
      if (dotProduct <= this.length) {
        val pointProjectionOnLine = new Vector(pointX - startX, pointY - startY).projectionOn(this.asUnitVector)
        return new Vector(pointX - startX, pointY - startY) - pointProjectionOnLine      
      }
      else {        
        return new Vector(pointX - endX, pointY - endY)
      }
    }
    else {     
      return lineToPointVector
    }
  }
  
  def distanceFrom(pointX : Double, pointY : Double) : Double = {    
    shortestVectorTo(pointX, pointY).length
  }
  
  override def toString : String = {
    return "Line(" + startX + "," + startY + "-" + endX + "," + endY + ")"
  }
}

class Circle(x: Double, y : Double, val r : Double) extends Shape(x, y) {
  
  override def toString : String = {
    return "Circle(" + x + "," + y + ") : " + r + "r)"
  }
}

/** 
  Rectangle is specified as w, h and center point (x, y) instead of left upper 
  and right lower corner so that rectangle can be at an angle if needed
  */
class Rectangle(x : Double, y : Double, val w : Double, val h : Double) extends Shape(x, y) {

  lazy val asLines = convertToLines
  lazy val minX = x - (w/2)
  lazy val minY = y - (h/2)
  lazy val maxX = x + (w/2)
  lazy val maxY = y + (h/2)
  lazy val side_down = asLines(0)
  lazy val side_right = asLines(1)
  lazy val side_up = asLines(2)
  lazy val side_left = asLines(3)
  
  def convertToLines : Buffer[Line] = {
    val lines = new ArrayBuffer[Line]()
    
    lines += new Line(minX, minY, maxX, minY)
    lines += new Line(maxX, minY, maxX, maxY)
    lines += new Line(maxX, maxY, minX, maxY)
    lines += new Line(minX, maxY, minX, minY)    
    
    return lines
  }
  
  /* Returns the side of the rectangle facing the given point.
   * Note: does not take corners into account but always returns one
   *       of the two sides of the corner
   * Note: does not take rotation into account, cartesian aligned
   * Note: if point is inside box returns always the same side
   */
  def facingSide(pointX : Double, pointY : Double) : Line = {
    val facingSide = 
      if (pointX < minX) {
        asLines(3)
      } 
      else if (pointX > maxX) {
        asLines(1)
      }
      else if (pointY < minY) { 
        asLines(0)      
      }
      else if (pointY > maxY) { 
        asLines(2)
      }
      else {        
        println("Point (" + x + "," + y + ") is inside " + this + ", do something!")
        asLines(0)
      }
      
    return facingSide
  }
   
  def overlaps(other : Rectangle) : Boolean = {
    return !((minY > other.maxY) || (maxY < other.minY) ||
             (maxX < other.minX) ||(minX > other.maxX))
  }
    
  override def toString : String = {
    return "Rectangle(" + x + "," + y + ") : " + w + "w, " + h + "h)"
  }  
}


