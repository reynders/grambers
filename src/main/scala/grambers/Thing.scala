package grambers

import java.awt._
import java.lang.Math._
import java.awt.geom._
import java.lang.System._

import org.jbox2d.dynamics._
import org.jbox2d.common._
import org.jbox2d.collision.shapes._

import scala.collection.immutable.List

abstract class Thing() {
  def center : Point = Point(0, 0)
  val body : Body                    
  var speed : Double = 0.0
  var mass = 0.0
  var doYourThing : ((Thing) => Unit) = (thing) => {}
 
  def draw(g2 : Graphics2D, position : Point)
  
  def drawDebugShapes(g2 : Graphics2D, position : Point)

  override def toString : String = "(" + center.x + "," + center.y + ")"
}

abstract class StaticThing(location : Point) extends Thing {
  lazy val body : Body = { val bd = new BodyDef()
                      bd.`type` = BodyType.STATIC
                      bd.position = new Vec2(location.x.toFloat, location.y.toFloat)
                      Universe.world.createBody(bd) }
  override def center : Point = location                    
}

abstract class MovingThing(location : Point) extends Thing {
  
  lazy val body : Body = { val bd = new BodyDef()
                           bd.`type` = BodyType.DYNAMIC
                           bd.position = new Vec2(location.x.toFloat, location.y.toFloat)
                           Universe.world.createBody(bd) }                    
                             
  override def center : Point = Point(body.getPosition.x, body.getPosition.y)
  
  def direction = toDegrees(body.getAngle)

  /* TODO
  def turn(degrees : Double) {
    direction += degrees
    direction %= 360
    if (direction < 0 ) direction = 360 + direction
  } */

  def turn(force : Double) = {
    println("Applying force " + force + " angle: " + body.getAngle + " as direction " + direction)
    body.setAngularVelocity(force.toFloat)
  }

  // TODO
  def accelerate(amount : Double) {
    speed += amount
  }
  
  // TODO: does not do what it says it does
  def setSpeedAndDirection(direction : Vector, speed : Double) {
    body.setLinearVelocity(new Vec2(direction.i.toFloat, direction.j.toFloat))
  }
}

class CircleMovingThing(var c : Point, val radius : Double) extends MovingThing(c) {
  
  var color = java.awt.Color.yellow
  val w = radius * 2
  val h = radius * 2
  
  {
    val cs = new CircleShape()    
    cs.m_radius = radius.toFloat
    
    val fd = new FixtureDef();
    fd.shape = cs;
    fd.density = 1f;
    fd.friction = 0.5f;
    fd.restitution = 0.5f;

    body.createFixture(fd)    
  }
  
  val image = new Ellipse2D.Double(center.x-w/2, center.y-h/2, radius*2, radius*2)

  def draw(g2 : Graphics2D, position : Point) {
    image.setFrame((position.x - w/2), (position.y-h/2), w, h)
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(image)
    g2.setPaint(originalPaintColor)
  }
  
  def drawDebugShapes(g2 : Graphics2D, position : Point) {
  }
  
  override def toString : String = {
    return "RoundThing" + super.toString + ":" + radius + "r"
  }
}

object CircleMovingThing {
  def apply(x : Double, y : Double, radius : Double, color : Color) : CircleMovingThing = {
    val ct = new CircleMovingThing(Point(x, y), radius)
    ct.color = color
    return ct
  }
}

class PolygonStaticThing(val c : Point, vertices : List[(Int, Int)]) extends StaticThing(c) {
  {
    val ps = new PolygonShape()
    val v = vertices.map(v => new org.jbox2d.common.Vec2(v._1, v._2))
    ps.set(v.toArray, v.size)    
    
    val fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 1.0f;
    fd.friction = 0.5f;
    fd.restitution = 0.5f;

    body.createFixture(fd)    
  }

  override def draw(g2 : Graphics2D, position : Point) = {
    if (Config.debugDrawShapes)
      drawDebugShapes(g2, position)
  }
  
  override def drawDebugShapes(g2 : Graphics2D, position : Point) {
  }
}

object PolygonStaticThing {

  def apply(c : Point, vertices : List[(Int, Int)]) : PolygonStaticThing = new PolygonStaticThing(c, vertices)

  def apply(c : Point, vertices : Array[Point]) : PolygonStaticThing = 
      new PolygonStaticThing(c, vertices.map { vertice => (vertice.x.toInt, vertice.y.toInt)}.toList)
}

class RectangleStaticThing(val c : Point, val w : Int, val h : Int) extends StaticThing(c) {
  {
    val ps = new PolygonShape()
    ps.setAsBox(w.toFloat/2, h.toFloat/2) // SetAsBox takes half w / h    
    
    val fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 1.0f;
    fd.friction = 0.5f;
    fd.restitution = 0.5f;

    body.createFixture(fd)    
  }

  override def draw(g2 : Graphics2D, position : Point) = {}
  
  override def drawDebugShapes(g2 : Graphics2D, position : Point) = {}
}

import java.awt.image._
import java.io._

class PolygonMovingThing(var c : Point, val sprite : Sprite) extends MovingThing(c) {

  {
    val ps = new PolygonShape()
    val v = sprite.polygonPoints.map(v => new org.jbox2d.common.Vec2(v.x.toInt, v.y.toInt))
    ps.set(v.toArray, v.size)    
    
    val fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 1.0f;
    fd.friction = 0.5f;
    fd.restitution = 0.5f;

    body.createFixture(fd)    
  }

  override def draw(g2: Graphics2D, position : Point) {
    if (Config.debugDrawShapes)
      drawDebugShapes(g2, position)

    val img = sprite.getCurrentImage(direction.toInt, false, Config.currentTimeMillis)

    g2.drawImage(img, (position.x - img.getWidth/2).toInt, (position.y-img.getHeight/2).toInt, null)    
  }
  
  override def drawDebugShapes(g2 : Graphics2D, position : Point) {

    val polygonShape = body.getFixtureList.getShape.asInstanceOf[org.jbox2d.collision.shapes.PolygonShape]
    val poly = new java.awt.Polygon()

    for (i <- 0 until polygonShape.getVertexCount) {
      // TODO: The jbox2d polygonshape points are relative to "centroid"
      //poly.addPoint(polygonShape.getVertex(i).x.toInt, polygonShape.getVertex(i).y.toInt)
      val wp = Point(body.getWorldPoint(polygonShape.getVertex(i)))
      //println("body position: " + body.getPosition + " lv: " + polygonShape.getVertex(i) + " wv: " + wp)
      poly.addPoint(wp.x.toInt, wp.y.toInt)
    }

    poly.translate(position.x.toInt - center.x.toInt, position.y.toInt - center.y.toInt)

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(Config.debugDrawShapesColor)
    g2.draw(poly)
    g2.setPaint(originalPaintColor)
  }
  
  override def toString : String = {
    return "PolygonMovingThing: " + super.toString
  }
}