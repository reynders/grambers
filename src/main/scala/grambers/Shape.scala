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
    case (line : Line, circle:Circle) => line.shortestVectorTo(circle.center).length <= circle.r
    case _ => println("Warning: do not know how to collide " + leftShape + " with " + rightShape)
              return false
  }

}

class Point(val x : Double, val y : Double) {

  def +(i : Int) : Point = {
    return(Point(x + i, y + i))
  }

  def -(i : Int) : Point = {
    return(Point(x - i, y - i))
  }

  def +(other : Point) : Point = {
    return(Point(x + other.x, y + other.y))
  }

  def -(other : Point) : Point = {
    return(Point(x - other.x, y - other.y))
  }

  implicit def IntToPoint(i:Int) : Point = new Point(i, i)

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
  def apply(x : String, y : String) : Point = new Point(x.toDouble, y.toDouble)
  def apply(x : Int, y : Int) : Point = new Point(x.toDouble, y.toDouble)
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

  def toPoints(nOfPoints : Int) : Array[Point] = {
    assert(nOfPoints >= 2)
    val dX : Double = (endX - startX) / nOfPoints
    val dY : Double = (endY - startY) / nOfPoints
    val points = ArrayBuffer[Point]()
    for(i <- 0 until (nOfPoints-1)) points += Point((startX + (i*dX)), (startY + (i*dY)))
    points += Point(endX, endY)
    return points.toArray
  }

  override def toString : String = {
    return "Line(" + startX + "," + startY + "-" + endX + "," + endY + ")"
  }
}

object Line {
  def apply(start:Point, end:Point) : Line = new Line(start.x, start.y, end.x, end.y)
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
class Rectangle(val minX:Double, val minY:Double, val maxX:Double, val maxY:Double) extends
                Shape(Point(((minX+maxX) / 2), ((minY+maxY) / 2))) {
  val lup = (minX, minY)
  val rlp = (maxX, maxY)
  lazy val w = maxX - minX
  lazy val lupPoint = Point(minX, minY)
  lazy val rlpPoint = Point(maxX, maxY)
  lazy val h = maxY - minY
  lazy val asLines = convertToLines
  lazy val side_down = asLines(0)
  lazy val side_right = asLines(1)
  lazy val side_up = asLines(2)
  lazy val side_left = asLines(3)

  def this(lup:(Double, Double), rlp:(Double, Double)) = this(lup._1, lup._2, rlp._1, rlp._2)

  def this(center:Point, w:Double, h:Double) = this(center.x-(w/2), center.y-(h/2),
                                                    center.x+(w/2), center.y+(h/2))

  def this(lup:Point, rlp:Point) = this(lup.x, lup.y, rlp.x, rlp.y)

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

  import scala.Math._
  def intersect(other:Rectangle) : Rectangle =
       if (overlaps(other))
          new Rectangle((max(minX, other.minX), max(minY, other.minY)),
                    (min(maxX, other.maxX), min(maxY, other.maxY)))
       else
          new Rectangle((0.0, 0.0), (0.0, 0.0))

  def contains(other:Rectangle) : Boolean =
        ((other.minX >= minX && other.maxX <= maxX) &&
        (other.minY >= minY && other.maxY <= maxY))

  def fitsIn(other:Rectangle) : Boolean = ((w <= other.w) && (h <= other.h))

  def limitBy(other:Rectangle) : Rectangle = {
    val mnX = if (minX > other.minX) minX else other.minX
    val mnY = if (minY > other.minY) minY else other.minY
    val mxX = if (maxX < other.maxX) maxX else other.maxX
    val mxY = if (maxY < other.maxY) maxY else other.maxY

    return Rectangle((mnX.toInt, mnY.toInt), (mxX.toInt, mxY.toInt))
  }

  def isSameSize(other:Rectangle) : Boolean = {
    w == other.w && h == other.h
  }

  def translate(by:(Double, Double)) : Rectangle =
          new Rectangle(minX + by._1, minY + by._2, maxX + by._1, maxY + by._2)

  def normalize : Rectangle = translate(-minX, -minY)

  override def equals(other : Any) = other match {
    case that : Rectangle => ((this.center == that.center) &&
                              (this.w == that.w) && (this.h == that.h))
    case _ => false
  }

  override def toString : String = {
    return "Rectangle((" + lup + "),(" + rlp + "))"
  }
}

object Rectangle {
  //def apply(lup:(Double, Double), rlp:(Double, Double)) : Rectangle = new Rectangle(lup._1, lup._2, rlp._1, rlp._2)
  def apply(lup:(Int, Int), rlp:(Int, Int)) : Rectangle = new Rectangle(lup._1.toDouble, lup._2.toDouble,
                                                                       rlp._1.toDouble, rlp._2.toDouble)
  def apply(lx:Int, ly:Int, rx:Int, ry:Int) : Rectangle = Rectangle((lx, ly), (rx, ry))
  def apply(rectangle:Rectangle, padding : Double) : Rectangle
     = new Rectangle(rectangle.minX - padding, rectangle.minY - padding,
                     rectangle.maxX + padding, rectangle.maxY + padding)
  def apply(lup:Point, rlp:Point) : Rectangle = new Rectangle(lup.x, lup.y, rlp.x, rlp.y)
  def apply(w:Int, h:Int) : Rectangle = new Rectangle(0, 0, w, h)

}

