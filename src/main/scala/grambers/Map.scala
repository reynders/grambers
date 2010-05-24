package grambers

import javax.swing._
import java.awt.image._
import java.awt._

class Map {
  val testTile : BufferedImage = javax.imageio.ImageIO.read(new java.io.File("resources/gfx/testtile.gif")).asInstanceOf[BufferedImage]

  def drawBackground(g2 : Graphics2D, center : Point, w : Int, h : Int) {
//    val backgroundImage = getBackground(Point(center.x - w/2, center.y - h/2)),
//                                        Point(center.x + w/2, center.y + w/2))
    val originalPaintColor = g2.getPaint()
    g2.setPaint(java.awt.Color.BLACK)
    g2.fillRect(center.x.toInt - w/2, center.y.toInt - h/2, center.x.toInt + w/2, center.y.toInt + w/2)
    g2.setPaint(originalPaintColor)                               
  }
  
  def getBackground(g2 : Graphics2D, leftUpperPoint : Point, rightLowerPoint : Point) {
//    val bg = new BufferedImage(image.getWidth, image.getHeight, BufferedImage.TYPE_4BYTE_ABGR)

  }
  
  def worldPointToTileIndex(worldPoint : Point) : Point = {
    return Point(0, 0)
  }
}
