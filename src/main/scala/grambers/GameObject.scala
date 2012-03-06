package grambers

import java.awt._
import java.awt.image._
import java.io._
import scala.collection.mutable.ArrayBuffer

class GameObject(val name : String) {
}

object GameObjectLoader {
  def load(fileName : String) : GameObject = {
    return new GameObject("dummy")
  }
}

class Sprite(val name : String, val img : BufferedImage, val w : Int, val h : Int,
             val rows : Int, val columns : Int, val xOffset : Int, val yOffset : Int,
             val rotates : Boolean, val rotationCount : Int) {

  lazy val imgW : Int = img.getWidth
  lazy val imgH : Int = img.getHeight
  lazy val images : Array[BufferedImage] = splitImageToSprites(img, w, h, rows, columns, xOffset, yOffset)

  def splitImageToSprites(img : BufferedImage, w : Int, h : Int, rows : Int, columns : Int,
                          xOffset : Int, yOffset : Int) : Array[BufferedImage] = {
    val imgs = new ArrayBuffer[BufferedImage]()
    for (y <- 0 to (rows-1))
      for (x <- 0 to (columns-1)) {
        imgs += img.getSubimage(x*w + (x*xOffset), y*h + (y*yOffset), w, h)
      }

      return imgs.toArray[BufferedImage]
  }

  override def toString : String = "Sprite " + name + "; w: " + w + " h:" + h + " rows: " + rows + " columns: " + columns +
                                   " xOffset:" + xOffset + " yOffset:" + yOffset
}

class DummySprite extends Sprite("dummy", new BufferedImage(1, 1, Config.imageType), 0, 0, 0, 0, 0, 0, false, 0) {}

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
                                                 (xml \ "gfx" \ "@rotates").text.equals("true"),
                                                 (xml \ "gfx" \ "@rotation_count").text.toInt)

  def load(gfxFileName : String, w : Int, h : Int, rows : Int, columns : Int, xOffset : Int, yOffset : Int,
           rotates : Boolean, rotationCount : Int) : Sprite = {
    val img : BufferedImage = javax.imageio.ImageIO.read(new File(gfxFileName)).asInstanceOf[BufferedImage]
    return new Sprite(gfxFileName, img, w, h, rows, columns, xOffset, yOffset, rotates, rotationCount)
  }
}
