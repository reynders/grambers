package grambers

import javax.swing._
import java.awt.image._
import java.awt._
import scala.collection.mutable._

class Map {

  val tileW = 128
  val tileH = 128
  //var tiles = fakeLoadMap("fakemap")
  var tileSets = new ArrayBuffer[TileSet]()
  var layers = new ArrayBuffer[Layer]()
    
  def drawBackground(g2 : Graphics2D, center : Point, w : Int, h : Int) {
    
    drawTiles(g2, Point(center.x - w/2, center.y - h/2),
                  Point(center.x + w/2, center.y + w/2))
  }
  
  def drawTiles(g2 : Graphics2D, leftUpperPoint : Point, rightLowerPoint : Point) {
   val lup = worldPointToTileIndex(leftUpperPoint)
   val rlp = worldPointToTileIndex(rightLowerPoint)

   for (x <- lup._1 to rlp._1) 
    for (y <- lup._2 to rlp._2) {
       //g2.drawImage(tiles(x)(y).image, x*tileW, y*tileH, null)
       val tileIndex = layers(0).tileMap(x)(y)
       g2.drawImage(getTile(0, (x, y)).image, x*tileW, y*tileH, null)
    }
  }
  
  def worldPointToTileIndex(worldPoint : Point) : (Int, Int) = ((worldPoint.x.toInt / tileW.toInt), 
                                                                (worldPoint.y.toInt / tileH.toInt))
  def getTile(layerId : Int, coord : (Int, Int)) : Tile = {
       val tileIndex = layers(layerId).tileMap(coord._1)(coord._2)
       return tileSets(tileIndex._1).tiles(tileIndex._2)                                                                  
  }
                                                                
}

class TileSet(name : String, id : Int) {
  val tiles = new ArrayBuffer[Tile]()
}

class Layer {
  val tileMap = ArrayBuffer[ArrayBuffer[(Int, Int)]]()
}

class Tile(val image : BufferedImage){
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

object MapLoader {
  
  def loadMap(mapName : String) : Map = {
    val map = new Map
    map.tileSets = loadTileSets
    map.layers = loadLayers
    return map
  }
  
  def loadTileSets : ArrayBuffer[TileSet] = {
    val testTileSet = new TileSet("testTileSet_1", 0)
    val testTile = Tile("resources/gfx/testtile.gif")
    testTileSet.tiles += testTile
    
    return ArrayBuffer(testTileSet)
  }

  def loadLayers : ArrayBuffer[Layer] = {
    val layers = new ArrayBuffer[Layer]()
    
    val testLayer = new Layer
    for (i <- 1 to 10) {
      val tileRow = new ArrayBuffer[(Int, Int)]
      for (i <- 1 to 10) {
        val tileRef = (0, 0) 
        tileRow += tileRef
      }
      testLayer.tileMap += tileRow
    }
    layers += testLayer
    
    return layers
  }
}
