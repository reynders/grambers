package grambers

import java.awt._
import java.awt.image._
import java.io._
import scala.collection.mutable.ArrayBuffer

class Sprite(val name : String, val img : BufferedImage, val w : Int, val h : Int,
             val rows : Int, val columns : Int, val xOffset : Int, val yOffset : Int,
             val animationKey : String, val animationFps : Int,
             val rotates : Boolean, val rotationCount : Int, val polygonPoints : Array[Point]) {

  lazy val imgW : Int = img.getWidth
  lazy val imgH : Int = img.getHeight
  lazy val images : Array[BufferedImage] = SpriteLoader.splitImageToSprites(img, w, h, rows, columns, xOffset, yOffset)
  lazy val rotatedImages : Array[Array[BufferedImage]] = SpriteLoader.createRotatedImages(images, rotationCount, rotates)

  var isAnimating = false
  var lastGetCurrentImageTimestamp : Long = 0

  def getCurrentImage(direction : Int, animate : Boolean, now : Long) : BufferedImage = {
    val index = getCurrentImageIndex(direction, animate, now)
    rotatedImages(index._1)(index._2)
  }

  def getCurrentImageIndex(direction : Int, animate : Boolean, now : Long) : (Int, Int)= {
    val directionFrameIndex = direction % 360 / (360 / rotationCount)
    var animationFrameIndex = 0

    if (animate) {
      if (isAnimating)
        animationFrameIndex = currentAnimationFrameIndex(now - lastGetCurrentImageTimestamp)
      else 
        isAnimating = true   
    
      lastGetCurrentImageTimestamp = now
    } else
      isAnimating = false

    return (animationFrameIndex, directionFrameIndex)
  }

  lazy val animationDtBetweenFramesInMs = 1000 / animationFps
  var activeAnimationFrameIndex : Int = 0

  def currentAnimationFrameIndex(dt : Long) : Int = {
    if (dt >= animationDtBetweenFramesInMs) {
      activeAnimationFrameIndex = (activeAnimationFrameIndex + 1) % images.size  
    }

    return activeAnimationFrameIndex
  }

  override def toString : String = "Sprite " + name + "; w: " + w + " h:" + h + " rows: " + rows + " columns: " + columns +
                                    " animationKey: " + animationKey + " animationFps: " + animationFps
                                   " xOffset:" + xOffset + " yOffset:" + yOffset +
                                   " rotates: " + rotates + " rotationCount: " + rotationCount + " polygonPoints: " + polygonPoints
}

class DummySprite extends Sprite("dummy", new BufferedImage(1, 1, Config.imageType), 0, 0, 0, 0, 0, 0, "dummy", 0, false, 0, new Array[Point](0)) {}

import scala.xml._

object SpriteLoader {

  def load(spriteFileName : String) : Sprite = {
    try {

      val sprite = parseSprite(XML.loadFile(spriteFileName))
      println("Loaded " + sprite)
      return sprite

    } catch {
      case e:java.io.FileNotFoundException => println("Unknown sprite: " + spriteFileName)
    }

    return new DummySprite()
  }

  def parseSprite(xml : NodeSeq) : Sprite = load((xml \ "gfx" \ "@file").text,
                                                 (xml \ "gfx" \ "@w").text.toInt,
                                                 (xml \ "gfx" \ "@h").text.toInt,
                                                 (xml \ "gfx" \ "@rows").text.toInt,
                                                 (xml \ "gfx" \ "@columns").text.toInt,
                                                 (xml \ "gfx" \ "@x_offset").text.toInt,
                                                 (xml \ "gfx" \ "@y_offset").text.toInt,
                                                 (xml \ "gfx" \ "@animation_key").text,
                                                 (xml \ "gfx" \ "@animation_fps").text.toInt,
                                                 (xml \ "gfx" \ "@rotates").text.equals("true"),
                                                 (xml \ "gfx" \ "@rotation_count").text.toInt,
                                                 Util.pointArrayStrToPointArray((xml \ "gfx" \ "@polygonPoints").text))

  def load(gfxFileName : String, w : Int, h : Int, rows : Int, columns : Int, xOffset : Int, yOffset : Int,
           animationKey : String, animationFps : Int,
           rotates : Boolean, rotationCount : Int, polygonPoints : Array[Point]) : Sprite = {
    val img : BufferedImage = javax.imageio.ImageIO.read(new File(gfxFileName)).asInstanceOf[BufferedImage]
    return new Sprite(gfxFileName, img, w, h, rows, columns, xOffset, yOffset,
                      animationKey, animationFps, rotates, rotationCount, polygonPoints)
  }

  def splitImageToSprites(img : BufferedImage, w : Int, h : Int, rows : Int, columns : Int,
                          xOffset : Int, yOffset : Int) : Array[BufferedImage] = {
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

}
