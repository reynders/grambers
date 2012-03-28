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

  def drawDebugShapes(g2 : Graphics2D, position : Point) {
    var fixture = body.getFixtureList

    while (fixture != null) {
      val shape = fixture.getShape

      if (shape.isInstanceOf[org.jbox2d.collision.shapes.PolygonShape]) {
        drawShape(g2, position, shape.asInstanceOf[PolygonShape])
      } else if (shape.isInstanceOf[org.jbox2d.collision.shapes.CircleShape]) {
        drawShape(g2, position, shape.asInstanceOf[CircleShape])
      }

      fixture = fixture.getNext
    }
  }

  def drawShape(g2 : Graphics2D, position : Point, polygonShape : PolygonShape) {
    val poly = new java.awt.Polygon()

    for (i <- 0 until polygonShape.getVertexCount) {
      // TODO: The jbox2d polygonshape points are relative to "centroid"
      val wp = Point(body.getWorldPoint(polygonShape.getVertex(i)))
      poly.addPoint(wp.x.toInt, wp.y.toInt)
    }

    poly.translate(position.x.toInt - center.x.toInt, position.y.toInt - center.y.toInt)

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(Config.debugDrawShapesColor)
    g2.draw(poly)
    g2.setPaint(originalPaintColor)
  }

  def drawShape(g2 : Graphics2D, position : Point, circleShape : CircleShape) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(Config.debugDrawShapesColor)
    val r = circleShape.m_radius.toInt
    val wp = body.getWorldPoint(circleShape.m_p)
    val x = wp.x.toInt - (center.x.toInt - position.x.toInt) - r
    val y = wp.y.toInt - (center.y.toInt - position.y.toInt) - r

    g2.drawOval(x, y, r*2, r*2)
    g2.setPaint(originalPaintColor)
  }

  // Called inside the game loop to allow Things to do stuff like animation, 
  // applying forces etc
  def step(dt : Double) {}

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
    fd.density = 0.0f;
    fd.friction = 0.5f;
    //fd.restitution = 0.5f;

    body.createFixture(fd)
  }

  override def draw(g2 : Graphics2D, position : Point) = {
    if (Config.debugDrawShapes) {
      drawDebugShapes(g2, position)
    }
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

  override def draw(g2 : Graphics2D, position : Point) = {
    if (Config.debugDrawShapes)
      drawDebugShapes(g2, position)
  }
}

import java.awt.image._
import java.io._

class SpriteMovingThing(var c : Point, val sprite : Sprite) extends MovingThing(c) {

  {
    sprite.massBodies.foreach { massBody =>
      println("SpriteMovingThing " + c + ": creating a fixture from " + massBody)
      body.createFixture(massBodyToFixture(massBody))
    }

    val md = new org.jbox2d.collision.shapes.MassData
    body.getMassData(md)
    println("Mass is: " + md.mass + " c " + md.center + " i " + md.I )
    /*
    md.I = 10.0f
    body.setMassData(md)
    */
  }

  def massBodyToFixture(massBody : MassBody) : FixtureDef = {
    val shape : org.jbox2d.collision.shapes.Shape = massBody match {
      case cmb : CircleMassBody =>
        val cs = new CircleShape()
        cs.m_p.set(new Vec2(cmb.c.x.toFloat, cmb.c.y.toFloat))
        cs.m_radius = cmb.r.toFloat
        cs
      case rmb : RectangleMassBody =>
        val ps = new PolygonShape()
        ps.setAsBox(rmb.w.toFloat/2, rmb.h.toFloat/2, new org.jbox2d.common.Vec2(rmb.c.x.toFloat, rmb.c.y.toFloat), 0)
        ps
      case pmb : PolygonMassBody =>
        val ps = new PolygonShape()
        val v = pmb.points.map(v => new org.jbox2d.common.Vec2(v.x.toInt, v.y.toInt))
        ps.set(v.toArray, v.size)
        ps.m_centroid.set(new Vec2(pmb.c.x.toFloat, pmb.c.y.toFloat))
        ps
      case _ =>
        println("Unknown MassBody type!!")
        val cs = new CircleShape()
        cs.m_radius = 0
        cs
    }

    val fd = new FixtureDef();
    fd.shape = shape;
    fd.density = massBody.density.toFloat;
    fd.friction = massBody.friction.toFloat;
    fd.restitution = massBody.restitution.toFloat;

    return fd
  }

  override def draw(g2: Graphics2D, position : Point) {
    if (Config.debugDrawShapes)
      drawDebugShapes(g2, position)

    val img = sprite.getCurrentImage(direction.toInt, true, Config.currentTimeMillis)

    g2.drawImage(img, (position.x - img.getWidth/2).toInt, (position.y-img.getHeight/2).toInt, null)
  }

  override def toString : String = {
    return "SpriteMovingThing: " + super.toString
  }
}

class Ship(c : Point, sprite : Sprite) extends SpriteMovingThing(c, sprite) {
  var turnLeft = false
  var turnRight = false
  var accelerate = false
  var reverse = false

  def applyForce(force : Force) {
    body.applyLinearImpulse(body.getWorldVector(force.forceVectorVec2),  body.getWorldPoint(force.applicationPointVec2))
  }

  override def step(dt : Double) {
    if (turnLeft) applyForce(sprite.forceMap("TURN_LEFT"))
    if (turnRight) applyForce(sprite.forceMap("TURN_RIGHT"))
    if (accelerate) applyForce(sprite.forceMap("ACCELERATE"))
    if (reverse) applyForce(sprite.forceMap("REVERSE"))
  }
}

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class ShipKeyboardController(ship : Ship, keyActionMap : scala.collection.immutable.Map[Int, String]) extends KeyAdapter {
  override def keyPressed(e : KeyEvent) = {
    val c = e.getKeyCode();
    if (keyActionMap.contains(c)) {
      val action = keyActionMap(c)
      action match {
        case "TURN_LEFT" => ship.turnLeft = true; println("Turning left")
        case "TURN_RIGHT" => ship.turnRight = true; println("Turning right")
        case "ACCELERATE"  => ship.accelerate = true; println("Accelerating")
        case "REVERSE"  => ship.reverse = true; println("Reversing")
        case _ => println("Unknown ship keyboard action " + action)
      }
    }
  }

  override def keyReleased(e : KeyEvent) = {
    val c = e.getKeyCode();
    if (keyActionMap.contains(c)) {
      val action = keyActionMap(c)
      action match {
        case "TURN_LEFT" => ship.turnLeft = false; println("Stop Turning left")
        case "TURN_RIGHT" => ship.turnRight = false; println("Stop Turning right")
        case "ACCELERATE"  => ship.accelerate = false; println("Stop Accelerating")
        case "REVERSE"  => ship.reverse = false; println("Stop Reversing")
      }
    }
  }
}

object ShipKeyboardController {
  def apply(ship : Ship) : ShipKeyboardController = new ShipKeyboardController(ship, scala.collection.immutable.Map(
    KeyEvent.VK_LEFT -> "TURN_LEFT",
    KeyEvent.VK_RIGHT -> "TURN_RIGHT",
    KeyEvent.VK_UP -> "ACCELERATE", 
    KeyEvent.VK_DOWN -> "REVERSE"))
}