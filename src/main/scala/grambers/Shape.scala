package grambers

import scala.collection.mutable._
import org.jbox2d.common._
import Util.log

abstract class Shape(val center : Point) {

  def x : Int = center.x
  def y : Int = center.y

  def this(x : Int, y : Int) = this(new Point(x, y))
}

class Point(var x : Int, var y : Int) {

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

  def set(fromX : Int, fromY : Int) : Point = {
    this.x = fromX;
    this.y = fromY;
    this
  }

  override def equals(that : Any) = that match {
    case point : Point => (this.x == point.x && this.y == point.y)
    case _ => false
  }

  override def toString : String = {
    return "P(" + x + "," + y + ")"
  }
}

object Point {
  def apply(x : Int, y : Int) : Point = new Point(x, y)
  def apply(x : Float, y : Float) : Point = new Point(x.toInt, y.toInt)
  def apply(x : String, y : String) : Point = new Point(x.toInt, y.toInt)
  def apply(x : Double, y : Double) : Point = new Point(x.toInt, y.toInt)
  def apply(point : Vec2) : Point = new Point(point.x.toInt, point.y.toInt)
}

class Line(val startX : Int, val startY : Int, val endX : Int, val endY : Int) extends Shape(startX, startY) {

  override def equals(line : Any) = line match {
    case that : Line =>
      (this.startX == that.startX && this.startY == that.startY &&
      this.endX == that.endX && this.endY == that.endY) ||
      (this.startX == that.endX && this.startY == that.endY &&
      this.endX == that.startX && this.endY == that.startY)
    case _ =>
      false
  }

  def toPoints(nOfPoints : Int) : Array[Point] = {
    assert(nOfPoints >= 2)
    val dX : Int = (endX - startX) / nOfPoints
    val dY : Int = (endY - startY) / nOfPoints
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

class Circle(center : Point, val r : Int) extends Shape(center) {

  def this (x: Int, y : Int, r : Int) = this(new Point(x, y), r)

  override def toString : String = {
    return "Circle(" + x + "," + y + ") : " + r + "r)"
  }
}

/**
  Rectangle is specified as w, h and center point (x, y) instead of left upper
  and right lower corner so that rectangle can be at an angle if needed
  */
class Rectangle(val minX:Int, val minY:Int, val maxX:Int, val maxY:Int) extends
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

  def this(lup:(Int, Int), rlp:(Int, Int)) = this(lup._1, lup._2, rlp._1, rlp._2)

  def this(center:Point, w:Int, h:Int) = this(center.x-(w/2), center.y-(h/2),
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
        log.error("Point (" + point.x + "," + point.y + ") is inside " + this + ", do something!")
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
          new Rectangle((0, 0), (0, 0))

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

  def translate(by:(Int, Int)) : Rectangle =
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
  //def apply(lup:(Int, Int), rlp:(Int, Int)) : Rectangle = new Rectangle(lup._1, lup._2, rlp._1, rlp._2)
  def apply(lup:(Int, Int), rlp:(Int, Int)) : Rectangle = new Rectangle(lup._1.toInt, lup._2.toInt,
                                                                       rlp._1.toInt, rlp._2.toInt)
  def apply(lx:Int, ly:Int, rx:Int, ry:Int) : Rectangle = Rectangle((lx, ly), (rx, ry))
  def apply(rectangle:Rectangle, padding : Int) : Rectangle
     = new Rectangle(rectangle.minX - padding, rectangle.minY - padding,
                     rectangle.maxX + padding, rectangle.maxY + padding)
  def apply(lup:Point, rlp:Point) : Rectangle = new Rectangle(lup.x, lup.y, rlp.x, rlp.y)
  def apply(w:Int, h:Int) : Rectangle = new Rectangle(0, 0, w, h)

}

