package grambers

import java.awt._
import java.awt.image._
import java.io._
import scala.collection.mutable.ArrayBuffer
import scala.xml._
import Util.log

class GameObject(val sprites : Array[Sprite], val massBodies : Array[MassBody], val forces : Array[Force]) {
  lazy val forceMap : scala.collection.immutable.Map[String, Force] = forces.map {force => (force.action, force)}.toMap[String, Force]
  lazy val actionToSpriteMap : scala.collection.immutable.Map[String, Sprite] = sprites.map {sprite => (sprite.action, sprite)}.toMap[String, Sprite]
  override def toString() : String = "GameObject: sprites " + sprites.size + " massBodies " + massBodies.size +
                                     " forces " + forces.size
}

object GameObject {
  def load(fileName : String) : GameObject = {
    try {
      val gameObject = parseGameObject(XML.loadFile(fileName))
      log.debug("Loaded " + gameObject)
      return gameObject
    } catch {
      case e:java.io.FileNotFoundException => log.error("Unknown game object: " + fileName)
    }

    return new GameObject(Array(), Array(), Array())
  }

  def parseGameObject(xml : NodeSeq) : GameObject = new GameObject(
      parseSprites(xml \ "sprite"),
      parseMassBodies(xml \ "mass_body"),
      parseForces(xml \ "force"))

  def parseSprites(xml : NodeSeq) : Array[Sprite] = xml.map {sprite =>
                                                             parseSprite(sprite)}.toArray[Sprite]

  def parseSprite(xml : NodeSeq) : Sprite = new Sprite((xml \ "@file").text,
                                                       (xml \ "@w").text.toInt,
                                                       (xml \ "@h").text.toInt,
                                                       (xml \ "@rows").text.toInt,
                                                       (xml \ "@columns").text.toInt,
                                                       (xml \ "@x_offset").text.toInt,
                                                       (xml \ "@y_offset").text.toInt,
                                                       (xml \ "@action").text,
                                                       Util.parseInt((xml \ "@animation_fps").text, 0),
                                                       (xml \ "@rotates").text.equals("true"),
                                                       (xml \ "@rotation_count").text.toInt)

  def parseMassBodies(xml : NodeSeq) : Array[MassBody] = xml.map{massBody =>
                                                                 parseMassBody(massBody)}.toArray[MassBody]

  def parseMassBody(xml : NodeSeq) : MassBody = {
    val massBody = (xml \\ "@type").text match {
      case "circle" =>
        val center = Util.strPointToPoint((xml \ "@center").text)
        val r = (xml \\ "@r").text.toDouble
        new CircleMassBody(center, r)
      case "rectangle" =>
        val center = Util.strPointToPoint((xml \ "@center").text)
        val w = (xml \\ "@w").text.toDouble
        val h = (xml \\ "@h").text.toDouble
        new RectangleMassBody(center, w, h)
      case "polygon" =>
        val center = Util.strPointToPoint((xml \ "@center").text)
        val points = Util.pointArrayStrToPointArray((xml \ "@points").text)
        new PolygonMassBody(center, points)
      case _ =>
        log.warn("Don't know how to parse fixture type " + (xml \\ "@type").text + " xml: \n" + xml)
        new CircleMassBody(Point(0,0), 0.0)
    }
    massBody.density = Util.parseDouble((xml \ "@density").text, 1.0)
    massBody
  }

  def parseForces(xml : NodeSeq) : Array[Force] = xml.map {force => parseForce(force)}.toArray[Force]

  def parseForce(xml : NodeSeq) : Force =
                new Force(Util.strPointToPoint((xml \ "@force_vector").text),
                          Util.strPointToPoint((xml \ "@application_point").text),
                          (xml \ "@action").text)
}

class Sprite(val name : String, val w : Int, val h : Int,
             val rows : Int, val columns : Int, val xOffset : Int, val yOffset : Int,
             val action : String, val animationFps : Int,
             val rotates : Boolean, val rotationCount : Int) {

  lazy val imgW : Int = img.getWidth
  lazy val imgH : Int = img.getHeight
  lazy val img : BufferedImage = javax.imageio.ImageIO.read(new File(name)).asInstanceOf[BufferedImage]
  lazy val images : Array[BufferedImage] = splitImageToSprites(img, w, h, rows, columns, xOffset, yOffset)
  lazy val rotatedImages : Array[Array[BufferedImage]] = createRotatedImages(images, rotationCount, rotates)

  def getCurrentImage(direction : Int, animate : Boolean, now : Long) : BufferedImage = {
    val index = getCurrentImageIndex(direction, animate, now)
    rotatedImages(index._1)(index._2)
  }

  var isAnimating = false
  lazy val animationDtBetweenFramesInMs = 1000 / animationFps
  var activeAnimationFrameIndex : Int = 0
  var activeAnimationFrameShownSince : Long = 0

  def getCurrentImageIndex(direction : Int, animate : Boolean, now : Long) : (Int, Int)= {
    val directionFrameIndex = if (direction >= 0)
                                direction % 360 / (360 / rotationCount)
                              else
                                ((360 + (direction % 360)) % 360) / (360 / rotationCount)

    if (animate) {
      if (isAnimating) {
        if ((now - activeAnimationFrameShownSince) > animationDtBetweenFramesInMs) {
          activeAnimationFrameIndex = (activeAnimationFrameIndex + 1) % images.size
          activeAnimationFrameShownSince = now
        }
      } else {
        isAnimating = true
        activeAnimationFrameShownSince = now
      }
    } else  {
      isAnimating = false
      activeAnimationFrameIndex = 0
    }

    return (activeAnimationFrameIndex, directionFrameIndex)
  }

  def splitImageToSprites(img : BufferedImage, w : Int, h : Int, rows : Int, columns : Int,
                          xOffset : Int, yOffset : Int) : Array[BufferedImage] = {
    log.debug("Splitting " + name + ": " + img.getWidth + " vs " + w + " c " + columns)
    val imgs = new ArrayBuffer[BufferedImage]()
    for (y <- 0 to (rows-1))
      for (x <- 0 to (columns-1)) {
        imgs += img.getSubimage(x*w + (x*xOffset), y*h + (y*yOffset), w, h)
      }

      return imgs.toArray[BufferedImage]
  }

  import java.awt.geom._
  import java.lang.Math._

  def createRotatedImages(images : Array[BufferedImage], rotationCount : Int, rotates: Boolean) : Array[Array[BufferedImage]] = {
    val rotatedImages = new ArrayBuffer[Array[BufferedImage]]()

    if (rotates)
      images.foreach { image => rotatedImages += createRotatedImages(image, rotationCount)}

    return rotatedImages.toArray
  }

  def createRotatedImages(img : BufferedImage, rotationCount : Int) : Array[BufferedImage] = {

    val images = new ArrayBuffer[BufferedImage]()

    for(i <- 0 until rotationCount) {
      //val rotatedImage = image.getGraphics().asInstanceOf[Graphics2D].getDeviceConfiguration().createCompatibleImage(image.getWidth, image.getHeight, Transparency.TRANSLUCENT)
      val diameter = Math.max(img.getWidth, img.getHeight)
      val rotatedImage = new BufferedImage(diameter, diameter, Config.imageType)
      val at = new AffineTransform()
      at.rotate(toRadians(i*(360/rotationCount)), diameter/2, diameter/2)
      at.translate(Math.abs(diameter-img.getWidth)/2, Math.abs(diameter-img.getHeight)/2)
      val g2d = rotatedImage.createGraphics.asInstanceOf[Graphics2D]
      g2d.drawImage(img, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0)
      images += rotatedImage
    }

    return images.toArray
  }

  override def toString : String = "Sprite " + name + "; w: " + w + " h:" + h + " rows: " + rows + " columns: " + columns +
                                    " action: " + action + " animationFps: " + animationFps
                                   " xOffset:" + xOffset + " yOffset:" + yOffset +
                                   " rotates: " + rotates + " rotationCount: " + rotationCount
}

abstract class MassBody(c : Point) {
  var density = 1.0
  var friction = 1.0
  var restitution = 0.5
}

case class CircleMassBody(c : Point, r : Double) extends MassBody(c) {}

case class RectangleMassBody(c : Point, w : Double, h : Double) extends MassBody(c) {}

case class PolygonMassBody(c : Point, points : Array[Point]) extends MassBody(c) {}

class Force(val forceVector : Point, val applicationPoint : Point, val action : String) {
  import org.jbox2d.common.Vec2
  lazy val forceVectorVec2 = new Vec2(forceVector.x.toFloat, forceVector.y.toFloat)
  lazy val applicationPointVec2 = new Vec2(applicationPoint.x.toFloat, applicationPoint.y.toFloat)
}

class DummySprite extends Sprite("dummy", 0, 0, 0, 0, 0, 0, "dummy", 0, false, 0) {}