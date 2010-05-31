package grambers

import javax.swing._
import java.awt.image._
import java.awt._
import scala.collection.mutable._

class Map {

  val tileW = 128
  val tileH = 128
  var tiles = fakeLoadMap("fakemap")
  
  // Create a fake 10x2 tile map
  def fakeLoadMap(mapName : String) : ArrayBuffer[ArrayBuffer[Tile]] = {
    tiles = new ArrayBuffer[ArrayBuffer[Tile]]
    for (i <- 1 to 10) {
      val tileRow = new ArrayBuffer[Tile]
      for (i <- 1 to 10) {
        tileRow += Tile("resources/gfx/testtile.gif")
      }
      tiles += tileRow
    }
    
    return tiles
  }
  
  def drawBackground(g2 : Graphics2D, center : Point, w : Int, h : Int) {
    
    drawTiles(g2, Point(center.x - w/2, center.y - h/2),
                  Point(center.x + w/2, center.y + w/2))
  }
  
  def drawTiles(g2 : Graphics2D, leftUpperPoint : Point, rightLowerPoint : Point) {
   val lup = worldPointToTileIndex(leftUpperPoint)
   val rlp = worldPointToTileIndex(rightLowerPoint)
//println("Drawing from " + lup + " to " + rlp + " (from " + leftUpperPoint + " to " + rightLowerPoint)   
   for (x <- lup._1 to rlp._1) 
     for (y <- lup._2 to rlp._2) 
       g2.drawImage(tiles(x)(y).image, x*tileW, y*tileH, null)
  }
  
  def worldPointToTileIndex(worldPoint : Point) : (Int, Int) = ((worldPoint.x.toInt / tileW.toInt), 
                                                                (worldPoint.y.toInt / tileH.toInt))
}

class TileSet(name : String, id : Int) {
  val tiles = new ArrayBuffer[Tile]()
}

class Layer {
  val tileMap = ArrayBuffer[ArrayBuffer[(Int, Int)]]()
}

class Tile (val image : BufferedImage){
  val w : Int = image.getWidth
  val h : Int = image.getHeight
}

object Tile {
  def apply(imageName : String) : Tile = {
    val image : BufferedImage = javax.imageio.ImageIO.read(new java.io.File(imageName)).asInstanceOf[BufferedImage]
    val tile = new Tile(image)
    return tile
  }
}
