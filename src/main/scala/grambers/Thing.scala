package grambers

import java.awt._
import java.lang.Math._
import java.awt.geom._

import org.jbox2d.dynamics._
import org.jbox2d.common._
import org.jbox2d.collision.shapes._

import scala.collection.immutable.List
import Util.log

abstract class Thing() {
  def center : Point = new Point(0, 0)
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
      poly.addPoint(wp.x, wp.y)
    }

    poly.translate(position.x - center.x, position.y - center.y)

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
    val x = wp.x - (center.x - position.x) - r
    val y = wp.y - (center.y - position.y) - r

    g2.drawOval(x.toInt, y.toInt, r*2, r*2)
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

  override def center : Point = {val bc = body.getPosition; Point(bc.x, bc.y)}

  def direction = toDegrees(body.getAngle)

  // TODO: does not do what it says it does
  def setSpeedAndDirection(direction : Vec2, speed : Double) {
    body.setLinearVelocity(direction)
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
      new PolygonStaticThing(c, vertices.map { vertice => (vertice.x, vertice.y)}.toList)
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

class GameObjectMovingThing(var c : Point, val gameObject : GameObject) extends MovingThing(c) {

  {
    gameObject.massBodies.foreach { massBody =>
      //  log.debug("SpriteMovingThing " + c + ": creating a fixture from " + massBody)
      body.createFixture(massBodyToFixture(massBody))
    }

    // TODO: set this in gameobject xml
    body.setAngularDamping(3)
  }

  var actions = scala.collection.mutable.Set[String]()

  def applyForce(force : Force) {
    body.applyLinearImpulse(body.getWorldVector(force.forceVectorVec2),  body.getWorldPoint(force.applicationPointVec2))
  }

  override def step(dt : Double) {
    actions.foreach { action =>
      if (gameObject.forceMap.contains(action)) {
        applyForce(gameObject.forceMap(action))
      }
    }
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
        val v = pmb.points.map(v => new org.jbox2d.common.Vec2(v.x, v.y))
        ps.set(v.toArray, v.size)
        ps.m_centroid.set(new Vec2(pmb.c.x.toFloat, pmb.c.y.toFloat))
        ps
      case _ =>
        log.warn("Unknown MassBody type!!")
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

    // TODO: Clean this up
    if (actions.isEmpty) {
      val img = gameObject.actionToSpriteMap("NO_ACTION").getCurrentImage(direction.toInt, true, Config.currentTimeMillis)
      g2.drawImage(img, (position.x - img.getWidth/2).toInt, (position.y-img.getHeight/2).toInt, null)
    } else {
      actions.foreach { action =>
        if (gameObject.actionToSpriteMap.contains(action)) {
          val img = gameObject.actionToSpriteMap(action).getCurrentImage(direction.toInt, true, Config.currentTimeMillis)
          g2.drawImage(img, (position.x - img.getWidth/2).toInt, (position.y-img.getHeight/2).toInt, null)
        }
      }
    }
  }

  override def toString : String = {
    return "SpriteMovingThing: " + super.toString
  }
}

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class GameObjectKeyboardController(gameObject: GameObjectMovingThing, keyActionMap : scala.collection.immutable.Map[Int, String]) extends KeyAdapter {
  override def keyPressed(e : KeyEvent) = {
    val c = e.getKeyCode();
    if (keyActionMap.contains(c)) {
      val action = keyActionMap(c)
      action match {
        case str : String => gameObject.actions += action
        case _ => log.debug("Unknown game object keyboard action " + action)
      }
    }
  }

  override def keyReleased(e : KeyEvent) = {
    val c = e.getKeyCode();
    if (keyActionMap.contains(c)) {
      val action = keyActionMap(c)
      action match {
        case str : String => gameObject.actions -= action
        case _ => log.debug("Unknown game object keyboard action " + action)
      }
    }
  }
}

object GameObjectKeyboardController {
  def apply(gameObject : GameObjectMovingThing) : GameObjectKeyboardController =
      new GameObjectKeyboardController(gameObject, scala.collection.immutable.Map(
                                        KeyEvent.VK_LEFT -> "TURN_LEFT",
                                        KeyEvent.VK_RIGHT -> "TURN_RIGHT",
                                        KeyEvent.VK_UP -> "ACCELERATE",
                                        KeyEvent.VK_DOWN -> "REVERSE"))
}