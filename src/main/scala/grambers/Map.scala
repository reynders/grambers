package grambers

import javax.swing._
import java.awt.image._
import java.awt._
import scala.collection.mutable._
import scala.xml._

class Map {

  val tileW = 128
  val tileH = 128
  //var tiles = fakeLoadMap("fakemap")
  var tileSets = new ArrayBuffer[TileSet]()
  var tiles = new ArrayBuffer[Tile]()
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
       val tileIndex = layers(0).tileMap(x)(y)
       g2.drawImage(getTile(0, (x, y)).image, x*tileW, y*tileH, null)
    }
  }
  
  def worldPointToTileIndex(worldPoint : Point) : (Int, Int) = ((worldPoint.x.toInt / tileW.toInt), 
                                                                (worldPoint.y.toInt / tileH.toInt))
  def getTile(layerId : Int, coord : (Int, Int)) : Tile = {
    val tileIndex = layers(layerId).tileMap(coord._1)(coord._2)
    return tiles(tileIndex._2-1)                                                                  
  }
}

class TileSet(val firstTileIndex:Int, val tiles:ArrayBuffer[Tile], val w:Int, val h:Int, val tileW:Int, val tileH:Int) {
}

object TileSet {

  def apply(xml : Node) : TileSet = 
     loadFromFile((xml \\ "image" \ "@source").text,
                  (xml \\ "@firstgid").text.toInt,
                  (xml \\ "@tilewidth").text.toInt,
                  (xml \\ "@tileheight").text.toInt,
                  (xml \\ "@spacing").text.toInt)

  def loadFromFile(fileName : String, firstTileIndex:Int, tileW : Int, tileH : Int, spacing : Int) : TileSet = {
    val image = javax.imageio.ImageIO.read(new java.io.File(fileName)).asInstanceOf[BufferedImage]
    val w = (image.getWidth-spacing)/(tileW+spacing)
    val h = (image.getHeight-spacing)/(tileH+spacing)
    val tiles = splitToTiles(image, w, h, tileW, tileH, spacing)
    return new TileSet(firstTileIndex, tiles, w, h, tileW, tileH)
  }
  
  def splitToTiles(image:BufferedImage, w:Int, h:Int, tileW:Int, tileH:Int, spacing:Int) : ArrayBuffer[Tile] = {
    val tiles : ArrayBuffer[Tile] = new ArrayBuffer[Tile]()
    for (x <- 0 until w) {
      val xSpacing = (x+1) * spacing
      for (y <- 0 until h) {
        val ySpacing = (y+1) * spacing
        val tileImage = new BufferedImage(tileW, tileH, image.getType)
        val g = tileImage.createGraphics
        g.drawImage(image, 0, 0, tileW, tileH, tileW*x+xSpacing, tileH*y+ySpacing, 
                                              (tileW*x+xSpacing)+tileW, (tileH*y+ySpacing)+tileH, null)
        g.dispose
        tiles += new Tile(tileImage)
      }
    }
    return tiles
  }
}

class Layer(val name : String, val w : Int, val h : Int) {
  var tileMap = Layer.createInitialTileMap(w, h) 
}

object Layer {
  def apply(xml : Node) : Layer = {
    val layer = new Layer((xml \ "@name").text, 
                          (xml \ "@width").text.toInt, 
                          (xml \ "@height").text.toInt)
                          
    layer.tileMap = parseLayerData((xml \\ "data").text.trim, layer.w, layer.h)
    return layer
  }
  
  // Awful shit!
  def parseLayerData(base64GzipTileMapString : String, w : Int, h : Int) : ArrayBuffer[ArrayBuffer[(Int, Int)]] = {
    val tileMap = createInitialTileMap(w, h)
    import net.iharder.Base64
    val byteArray : Array[Byte] = Base64.decode(base64GzipTileMapString)
    for (i <- 0 until byteArray.size) {
      if (i%4 == 0) {
        val tileIndex = BigInt(byteArray.slice(i, i+4).reverse).toInt
        val coord : (Int, Int) = (((i/4)%w), ((i/4)/w))
        // println(i + ":" + coord + "=" + tileIndex)
        tileMap(coord._1)(coord._2) = absoluteTileIndexToTileSetAndIndex(tileIndex)
      }
    }

    return tileMap
  }

  def createInitialTileMap(w : Int, h : Int) : ArrayBuffer[ArrayBuffer[(Int, Int)]] ={
    val tileMap = new ArrayBuffer[ArrayBuffer[(Int, Int)]]()
    for (x <- 0 until w) {
      val column = new ArrayBuffer[(Int, Int)](h)
      for (y <- 0 until h) {
        column += ((0, 0))
      }      
      tileMap += column
    }
    return tileMap      
  }

  def absoluteTileIndexToTileSetAndIndex(absoluteTileIndex : Int) : (Int, Int) = {
    return (0, absoluteTileIndex)
  }                                                                

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
  
  def loadMap(mapFileName : String) : Map = {
    var map = new Map
    
    try {
      map = parseMapFromXml(XML.loadFile(mapFileName))
    } catch {
      case e:java.io.FileNotFoundException => println("Unknown map " + mapFileName)
    }

    println("Loaded map " + mapFileName + " with " + map.layers.size + " layers and " + 
             map.tiles.size + " tiles")
    
    return map
  }
  
  def parseMapFromXml(mapXml : Elem) : Map = {
    val map = new Map
    map.tileSets = parseTileSets(mapXml)
    map.layers = parseLayers(mapXml)
    map.tiles = createSingleTileMapFromManyTileSets(map.tileSets)
    return map
  }

  
  def parseTileSets(mapXml:Elem) : ArrayBuffer[TileSet] = {
    val tileSets = new ArrayBuffer[TileSet]()
    val tileSetElem = mapXml \\ "tileset"
    
    tileSetElem.foreach { tileSetNode => 
      val tileSet = TileSet(tileSetNode)
      tileSets += tileSet
    }
    return tileSets
  }

  def parseLayers(mapXml : Elem) : ArrayBuffer[Layer] = {
    val layers = new ArrayBuffer[Layer]()
    
    val layerElem = mapXml \\ "layer"
    
    layerElem.foreach { layerNode => 
      val layer = Layer(layerNode)
      layers += layer
    }
    return layers
  }
  
  def createSingleTileMapFromManyTileSets(tileSets:ArrayBuffer[TileSet]) : ArrayBuffer[Tile] = {
    tileSets.sortWith((l:TileSet, r:TileSet)=>{l.firstTileIndex > r.firstTileIndex})
    val tiles = new ArrayBuffer[Tile]()
    tileSets.foreach(tileSet => tiles ++= tileSet.tiles)
    return tiles
  }
}

