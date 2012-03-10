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
  
  var direction = toDegrees(body.getAngle)

  // TODO
  def turn(degrees : Double) {
    direction += degrees
    direction %= 360
    if (direction < 0 ) direction = 360 + direction
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

/*
class Wall(val start:Point, val end:Point) extends Thing(Point(end.x - start.x, end.y-start.y), 0, 0) extends StaticThing {
  def shape : Shape = {
    Line(start, end)
  }
  def draw(g2 : Graphics2D, position : Point ) = {}
  
  override def toString : String = {
    return "Wall(" + start + "," + end + ")"
  }
}*/

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

    val polygonShape = body.getFixtureList.getShape.asInstanceOf[org.jbox2d.collision.shapes.PolygonShape]
    val poly = new java.awt.Polygon()

    for (i <- 0 until polygonShape.getVertexCount) {
      // TODO: The jbox2d polygonshape points are relative to "centroid"
      poly.addPoint(polygonShape.getVertex(i).x.toInt, polygonShape.getVertex(i).y.toInt)
    }

    poly.translate(position.x.toInt, position.y.toInt)

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(Config.debugDrawShapesColor)
    g2.draw(poly)
    g2.setPaint(originalPaintColor)
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

/*
      extends PolygonStaticThing(lup, Array((lup.x.toInt, lup.y.toInt), (lup.x.toInt + w, lup.y.toInt),
                                            (lup.x.toInt + w, lup.y.toInt + h), (lup.x.toInt, lup.y.toInt + h)).toList)*/

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
      poly.addPoint(polygonShape.getVertex(i).x.toInt, polygonShape.getVertex(i).y.toInt)
    }

    poly.translate(position.x.toInt, position.y.toInt)

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

/*
object RoundThing {
  def apply(x : Double, y : Double, r : Double, mass : Double, color: Color, speed : Double, direction : Double) : RoundThing = {
    val ball = new RoundThing(Point(x, y), r)
    ball.mass = mass
    ball.color = color
    ball.speed = speed
    ball.direction = direction
    return ball
  }
}

class Box(c : Point, w : Double, h : Double) extends Thing(c, w, h) with StaticThing {
  
  def this(w : Double, h : Double) = this(Point(0, 0), w, h)
  
  var color = java.awt.Color.black

  def shape : Shape = {
    new Rectangle((center.x-(w/2), center.y-(h/2)), (center.x+(w/2), center.y+(h/2)))
  }
  
  val image = new Rectangle2D.Double(center.x - w/2, center.y-h/2, w, h)
  
  def draw(g2: Graphics2D, position : Point) {    
    image.setFrame((position.x - w/2), (position.y-h/2), w, h)
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(image)
    g2.setPaint(java.awt.Color.green)
    g2.drawRect(position.x.toInt, position.y.toInt, 2, 2)
    g2.setPaint(originalPaintColor)
  }
  
  override def toString : String = {
    return "Box" + super.toString + "," + w + "w," + h + "h"
  }
}

object Box {
  def apply(x : Double, y : Double, w : Double, h : Double, color : Color) : Box = {
    val box = new Box(new Point(x, y), w, h)
    box.color = color
    return box
  }
}

import java.awt.image._
import java.io._

class ImageRoundThing(var cntr : Point, val rad:Double, fileName : String) extends RoundThing(cntr, rad) with MovingThing {
  
  //def this(radius : Double) = this(new Point(0, 0), radius)
  val ROTATED_IMAGE_COUNT = 36   
  val img : BufferedImage = javax.imageio.ImageIO.read(new File(fileName)).asInstanceOf[BufferedImage]
  val rotatedImage = createRotatedImages(img)
  
  import scala.collection.mutable._
  def createRotatedImages(image : BufferedImage) : Buffer[Image] = {
    val images = new ArrayBuffer[Image]()

    for(i <- 0 until ROTATED_IMAGE_COUNT) {
      //val rotatedImage = image.getGraphics().asInstanceOf[Graphics2D].getDeviceConfiguration().createCompatibleImage(image.getWidth, image.getHeight, Transparency.TRANSLUCENT)      
      val diameter = Math.max(image.getWidth, image.getHeight)
      val rotatedImage = new BufferedImage(diameter, diameter, Config.imageType)
      val at = new AffineTransform()
      at.rotate(toRadians(i*(360/ROTATED_IMAGE_COUNT)), diameter/2, diameter/2)
      at.translate(Math.abs(diameter-image.getWidth)/2, Math.abs(diameter-image.getHeight)/2)
      val g2d = rotatedImage.createGraphics.asInstanceOf[Graphics2D]      
      g2d.drawImage(image, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0)
      images += rotatedImage
    }
    
    return images
  }
    
  def getRotatedImageNumberBasedOnDirection : Int = direction.toInt / (360 / ROTATED_IMAGE_COUNT)
  
  override def draw(g2: Graphics2D, position : Point) {
    g2.drawImage(rotatedImage(getRotatedImageNumberBasedOnDirection), (position.x - w/2).toInt, (position.y-h/2).toInt, null)
  }

  override def toString : String = {
    return super.toString + ":" + radius + "r"
  }
}

object ImageRoundThing {
  def apply(x : Double, y : Double, r : Double, mass : Double, fileName : String, speed : Double, direction : Double) : ImageRoundThing = {
    val ball = new ImageRoundThing(Point(x, y), r, fileName)
    ball.mass = mass
    ball.speed = speed
    ball.direction = direction
    return ball
  }
}
*/
