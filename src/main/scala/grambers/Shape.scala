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
    case (circle: Circle, line : Line) => line.shortestVectorTo(circle.center).unitVector
    case (line : Line, circle: Circle) => line.shortestVectorTo(circle.center).unitVector
    case (rectangle : Rectangle, circle : Circle) => rectangle.facingSide(circle.center).shortestVectorTo(circle.center).unitVector
    case (circle : Circle, rectangle : Rectangle) => rectangle.facingSide(circle.center).shortestVectorTo(circle.center).unitVector
    case (leftCircle : Circle, rightCircle : Circle) => new Vector((rightCircle.x - leftCircle.x), (rightCircle.y - leftCircle.y)).unitVector
    case _ => println("Do not know how to calculate collisionUnitVector between " + leftShape + " and " + rightShape)
              new Vector(1, 0)
  }
  
  def collidesWith(leftShape : Shape, rightShape : Shape) : Boolean = (leftShape, rightShape) match {
    case (rectangle : Rectangle, circle : Circle) => circle.r >= rectangle.facingSide(circle.center).distanceFrom(circle.center)
    case (circle : Circle, rectangle : Rectangle) => circle.r >= rectangle.facingSide(circle.center).distanceFrom(circle.center) 
    case (leftCircle : Circle, rightCircle : Circle) => !((leftCircle.r + rightCircle.r) < leftCircle.distanceFrom(rightCircle))
    case (leftRectangle: Rectangle, rightRectangle : Rectangle) => leftRectangle.overlaps(rightRectangle)
    case _ => return false  
  }
  
}

class Point(val x : Double, val y : Double) {
  override def equals(that : Any) = that match {    
    case point : Point => (this.x == point.x && this.y == point.y)
    case _ => false      
  }

  override def toString : String = {
    return "P(" + x + "," + y + ")"
  }
}

object Point {
  def apply(x : Double, y : Double) : Point = new Point(x, y)

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
 
  def shortestVectorTo(point : Point) : Vector = {
    val lineToPointVector = new Vector(point.x - startX, point.y - startY)
    val dotProduct = lineToPointVector.dot(this.asUnitVector)

    if (dotProduct > 0) {
      if (dotProduct <= this.length) {
        val pointProjectionOnLine = new Vector(point.x - startX, point.y - startY).projectionOn(this.asUnitVector)
        return new Vector(point.x - startX, point.y - startY) - pointProjectionOnLine      
      }
      else {        
        return new Vector(point.x - endX, point.y - endY)
      }
    }
    else {     
      return lineToPointVector
    }
  }
  
  def distanceFrom(point : Point) : Double = {    
    shortestVectorTo(point).length
  }
  
  override def toString : String = {
    return "Line(" + startX + "," + startY + "-" + endX + "," + endY + ")"
  }
}

class Circle(center : Point, val r : Double) extends Shape(center) {
  
  def this (x: Double, y : Double, r : Double) = this(new Point(x, y), r)
  
  override def toString : String = {
    return "Circle(" + x + "," + y + ") : " + r + "r)"
  }
}

/** 
  Rectangle is specified as w, h and center point (x, y) instead of left upper 
  and right lower corner so that rectangle can be at an angle if needed
  */
class Rectangle(center : Point, val w : Double, val h : Double) extends Shape(center) {
  
  lazy val asLines = convertToLines
  lazy val minX = center.x - (w/2)
  lazy val minY = center.y - (h/2)
  lazy val maxX = center.x + (w/2)
  lazy val maxY = center.y + (h/2)
  lazy val side_down = asLines(0)
  lazy val side_right = asLines(1)
  lazy val side_up = asLines(2)
  lazy val side_left = asLines(3)
  
  def convertToLines : Array[Line] = {
    val lines = new ArrayBuffer[Line]()
    
    lines += new Line(minX, minY, maxX, minY)
    lines += new Line(maxX, minY, maxX, maxY)
    lines += new Line(maxX, maxY, minX, maxY)
    lines += new Line(minX, maxY, minX, minY)    
    
    return lines.toArray
  }
  
  /* Returns the side of the rectangle facing the given point.
   * Note: does not take corners into account but always returns one
   *       of the two sides of the corner
   * Note: does not take rotation into account, cartesian aligned
   * Note: if point is inside box returns always the same side
   */
  def facingSide(point : Point) : Line = {
    val facingSide = 
      if (point.x < minX) {
        asLines(3)
      } 
      else if (point.x > maxX) {
        asLines(1)
      }
      else if (point.y < minY) { 
        asLines(0)      
      }
      else if (point.y > maxY) { 
        asLines(2)
      }
      else {        
        println("Point (" + point.x + "," + point.y + ") is inside " + this + ", do something!")
        asLines(0)
      }
      
    return facingSide
  }
   
  def overlaps(other : Rectangle) : Boolean = {
    return !((minY > other.maxY) || (maxY < other.minY) ||
             (maxX < other.minX) ||(minX > other.maxX))
  }
  
  def contains(other:Rectangle) : Boolean = 
        ((other.minX >= minX && other.maxX <= maxX) &&
        (other.minY >= minY && other.maxY <= maxY))
   
  override def toString : String = {
    return "Rectangle(" + center.x + "," + center.y + ") : " + w + "w, " + h + "h)"
  }  
}

object Rectangle {

  def apply(lx:Int, ly:Int, rx:Int, ry:Int) : Rectangle = Rectangle((lx, ly), (rx, ry))
  
  def apply(lup:(Int, Int), rlp:(Int, Int)) : Rectangle = {
    val w = (rlp._1 - lup._1) + 1
    val h = (rlp._2 - lup._2) + 1
    return new Rectangle(Point((lup._1 + (w/2).toDouble), (lup._2 + (h/2)).toDouble), w.toDouble, h.toDouble)
  }
}

