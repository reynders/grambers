package grambers

import java.awt._
import java.lang.Math._
import java.awt.geom._
import java.lang.System._

abstract class Thing (var center : Point, val w:Double, val h:Double) {

    def this(w : Double, h : Double) = this(new Point(0, 0), w, h)
    var speed : Double = 0.0 // "pixels" per second
    var direction : Double = 0 // 0-360
    var mass : Double = 1.0
    var doYourThing : ((Thing) => Unit) = (thing) => {}

    def turn(degrees : Double) {
        direction += degrees
        direction %= 360
        if (direction < 0 ) direction = 360 + direction
    }

    def collidesWith(thing : Thing) : Boolean = {
      Shape.collidesWith(this.shape, thing.shape)
    }
  
    def accelerate(amount : Double) {
      speed += amount
    }

    def xSpeed : Double = {
      return speed * cos(toRadians(direction))
    }
    
    def ySpeed : Double = {
      return speed * sin(toRadians(direction))
    }

    def setSpeedAndDirection(xSpeed : Double, ySpeed : Double) {
      speed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed)
      direction = toDegrees(atan2(ySpeed, xSpeed))
      
      if (ySpeed < 0) direction += 360
    }
    
    def setSpeedAndDirection(vector : Vector) {
      setSpeedAndDirection(vector.i, vector.j)
    }
 
    def distanceFrom(otherThing : Thing) : Double = {
      val xDiff = Math.abs(otherThing.center.x - center.x)
      val yDiff = Math.abs(otherThing.center.y - center.y)
      
      return Math.sqrt((xDiff*xDiff) + (yDiff*yDiff)) 
    }
     
    def shape : Shape
    
    def draw(g2 : Graphics2D, position : Point )
        
    override def toString : String = {
        "(" + center.x + "," + center.y + "):(" + w + "w," + h + "h):" + speed + "p/s:" + direction + "dg"
    }
}


trait StaticThing extends Thing {
  override def setSpeedAndDirection(xSpeed : Double, ySpeed : Double) = {}
}

trait MovingThing extends Thing {
}

class RoundThing(var c : Point, val radius:Double) extends Thing(c, radius*2, radius*2) with MovingThing {
  
  def this(radius : Double) = this(new Point(0, 0), radius)
  
  var color = java.awt.Color.yellow
   
  def shape : Shape = {      
    new Circle(this.center.x, this.center.y, radius)
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
    new Rectangle(new Point(center.x, center.y), w, h)
  }
  
  val image = new Rectangle2D.Double(center.x - w/2, center.y-h/2, w, h)
  
  def draw(g2: Graphics2D, position : Point) {    
    image.setFrame((position.x - w/2), (position.y-h/2), w, h)
    //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    val originalPaintColor = g2.getPaint()
    g2.setPaint(color)
    g2.fill(image)
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
    box.mass = Math.MAX_DOUBLE / 2 // Kludge to make sure collisions with static objects work correctly
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
    //val g2d = image.getGraphics.asInstanceOf[Graphics2D]

    for(i <- 0 until ROTATED_IMAGE_COUNT) {
      val rotatedImage = new BufferedImage(image.getWidth, image.getHeight, BufferedImage.TYPE_4BYTE_ABGR)
      val g2d = rotatedImage.createGraphics.asInstanceOf[Graphics2D]
      
      val at = AffineTransform.getRotateInstance(toRadians(i*(360/ROTATED_IMAGE_COUNT)), image.getWidth/2, image.getHeight/2)

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

      g2d.drawImage(image, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0)
      images += rotatedImage
    }
    
    return images
  }
    
  def getRotatedImageNumberBasedOnDirection : Int = direction.toInt / (360 / ROTATED_IMAGE_COUNT)
  
var previousX = 0

  override def draw(g2: Graphics2D, position : Point) {

var currentX = position.x.toInt
if (previousX != currentX) {    
//  println(Config.currentTimeMillis + " : Drawing to x " + currentX + " : " + (currentX - previousX))
  previousX = currentX
}

    g2.drawImage(rotatedImage(getRotatedImageNumberBasedOnDirection), (position.x - w/2).toInt, (position.y-h/2).toInt, null)
  }

  override def toString : String = {
    return "RoundThing" + super.toString + ":" + radius + "r"
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

