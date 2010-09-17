package grambers

import javax.swing._
import java.awt.image._
import java.awt._
import scala.collection.mutable._
import scala.xml._
import scala.actors._
import scala.actors.Actor._

class Map(val wInTiles:Int, val hInTiles:Int, val tileW:Int, val tileH:Int, 
val tileSets:Array[TileSet], val layers:Array[Layer], val tiles:Array[Tile]) {
  
  def w = tileW * wInTiles
  def h = tileH * hInTiles  
  
  val TILE_BUFFER_PADDING_TILES = 2
  var bgImageWorldLup : (Double, Double) = _
  var bgImage : BackgroundImage = new BackgroundImage(new BufferedImage(1, 1, Config.imageType), Rectangle((0,0), (1,1)))
  
  import scala.actors.Futures._
  var myFuture : Future[BackgroundImage] = _
  var creatingNewBgImage = false
  
  def getBackgroundImage(center : Point, w : Int, h : Int) : BackgroundImage = {
    val windowLup = Point(center.x - w/2, center.y - h/2)
    val windowRlp = Point(center.x + w/2, center.y + w/2)
    val tileLup = worldPointToTileIndex(windowLup)
    val tileRlp = worldPointToTileIndex(windowRlp)

// TODO: Add buffering so that we do not recreate the background too late    
    if (!worldRectangleToTileRectangle(bgImage.worldCoordinates).contains(
        Rectangle(tileLup, tileRlp)) && !creatingNewBgImage) {     
      creatingNewBgImage = true
      myFuture = future {createBackgroundImageFromTiles(tileLup, tileRlp)}       
    }
    
    if (myFuture.isSet) {
      bgImage = myFuture()
      creatingNewBgImage = false
    }
    
    return bgImage.getVisiblePart(Rectangle(windowLup, windowRlp))
  }

  import scala.collection.mutable.ArrayBuffer
  
  var bgImages = ArrayBuffer[BackgroundImage](new BackgroundImage(new BufferedImage(1, 1, Config.imageType), Rectangle(0, 0, 1, 1)), 
                                              new BackgroundImage(new BufferedImage(1, 1, Config.imageType), Rectangle(0, 0, 1, 1)))
  
  
  def getBgImage(bgImageInTiles : Rectangle) : BackgroundImage = {
    val worldCoordinates = new Rectangle(bgImageInTiles.minX*tileW, bgImageInTiles.minY*tileH,
                                        ((bgImageInTiles.maxX+1)*tileW)-1, ((bgImageInTiles.maxY+1)*tileH)-1)
    val imageW = (bgImageInTiles.w.toInt+1)*tileW
    val imageH = (bgImageInTiles.h.toInt+1)*tileH
    
    bgImages = bgImages.reverse
    
    val image = if (worldCoordinates.fitsIn(bgImages(0).worldCoordinates)) bgImages(0).image 
                else new BufferedImage(imageW, imageH, Config.imageType)

    bgImages(0) = new BackgroundImage(image, worldCoordinates)      
    return bgImages(0)
  }
  
  def createBackgroundImageFromTiles(oLup:(Int,Int), oRlp:(Int,Int)) : BackgroundImage = {
    val bgImageAsTiles = Rectangle(Rectangle(oLup, oRlp), TILE_BUFFER_PADDING_TILES).limitBy(Rectangle((0,0), (wInTiles-1, hInTiles-1)))
    val bgImage = getBgImage(bgImageAsTiles)
    
    val lup = (bgImageAsTiles.minX.toInt, bgImageAsTiles.minY.toInt)
    val rlp = (bgImageAsTiles.maxX.toInt, bgImageAsTiles.maxY.toInt)

    for (y <- lup._2 to rlp._2) {
      for (x <- lup._1 to rlp._1) {
        bgImage.bgGraphics.drawImage(getTile(0, x, y).image, (x-lup._1)*tileW, (y-lup._2)*tileH, null)
      }
    }

//println("Created bg image from tiles " + Rectangle(lup, rlp) + ", in pixels (" + bgImage.image.getWidth + "," + bgImage.image.getHeight + ")")    
    return bgImage
  }
  
  def worldPointToTileIndex(worldPoint : Point) : (Int, Int) = {
    var x = worldPoint.x.toInt
    var y = worldPoint.y.toInt
    if (x < 0) x = 0 else if (x >= w) x = w-1
    if (y < 0) y = 0 else if (y >= h) y = h-1 
    val tx = (x / tileW.toInt)
    val ty = (y / tileH.toInt)
    assert ((tx >= 0 && tx < wInTiles && ty >= 0 && ty < hInTiles),
            "tx: " + tx + "(max value " + (wInTiles-1) + "), ty: " + ty + 
            "(max value " + (hInTiles-1) + ")")
            
    return (tx, ty)
  }
  
  def worldRectangleToTileRectangle(worldRectangle:Rectangle) : Rectangle = {
    val lup = worldPointToTileIndex(worldRectangle.lupPoint)
    val rlp = worldPointToTileIndex(worldRectangle.rlpPoint)
    return new Rectangle(lup._1,lup._2, rlp._1, rlp._2)
  }
  
  def getTile(layerId : Int, x:Int, y:Int) : Tile = {
    val tileIndex = layers(layerId).tileMap(x)(y)
    return tiles(tileIndex._2-1)                                                                  
  }
}

class DummyMap extends Map(1, 1, 1, 1, new Array[TileSet](0), new Array[Layer](0), new Array[Tile](0)) {
}

class BackgroundImage(val image:BufferedImage, val worldCoordinates:Rectangle) {
  lazy val bgGraphics : Graphics2D = image.createGraphics
  lazy val lup = (worldCoordinates.lup._1.toInt, worldCoordinates.lup._2.toInt)
    
  def getVisiblePart(visibleWindow : Rectangle) : BackgroundImage = {
    val bgX = lup._1
    val bgY = lup._2   
    val bgImageVisiblePart = 
         Rectangle(bgX, bgY, bgX + image.getWidth, bgY + image.getHeight).intersect(
         visibleWindow).translate(-bgX, -bgY)  
    val visiblePartWorldCoordinates = new Rectangle(bgImageVisiblePart.lup._1 + bgX, bgImageVisiblePart.lup._2 + bgY,
                                                bgImageVisiblePart.rlp._1 + bgX, bgImageVisiblePart.rlp._2 + bgY)
   
    val visiblePartImage = image.getSubimage(bgImageVisiblePart.lup._1.toInt, bgImageVisiblePart.lup._2.toInt,
                                             bgImageVisiblePart.w.toInt, bgImageVisiblePart.h.toInt)
    return new BackgroundImage(visiblePartImage, visiblePartWorldCoordinates)
  }
}

class TileSet(val firstTileIndex:Int, val tiles:Array[Tile], val w:Int, val h:Int, val tileW:Int, val tileH:Int) {
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
  
  def splitToTiles(image:BufferedImage, w:Int, h:Int, tileW:Int, tileH:Int, spacing:Int) : Array[Tile] = {
    val tiles : ArrayBuffer[Tile] = new ArrayBuffer[Tile]()
    for (y <- 0 until h) {
      val ySpacing = (y+1) * spacing
      for (x <- 0 until w) {
        val xSpacing = (x+1) * spacing
        val tileImage = image.getSubimage(tileW*x+xSpacing, tileH*y+ySpacing, tileW, tileH)
        tiles += new Tile(tileImage)
      }
    }
    return tiles.toArray[Tile]
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
  def parseLayerData(base64GzipTileMapString : String, w : Int, h : Int) : Array[Array[(Int, Int)]] = {
    val tileMap = createInitialTileMap(w, h)
    import net.iharder.Base64
    val byteArray : Array[Byte] = Base64.decode(base64GzipTileMapString)
    for (i <- 0 until byteArray.size) {
      if (i%4 == 0) {
        val tileIndex = BigInt(byteArray.slice(i, i+4).reverse).toInt
        val coord : (Int, Int) = (((i/4)%w), ((i/4)/w))
        tileMap(coord._1)(coord._2) = absoluteTileIndexToTileSetAndIndex(tileIndex)
      }
    }

    return tileMap
  }

  def createInitialTileMap(w : Int, h : Int) : Array[Array[(Int, Int)]] ={
    val tileMap = new ArrayBuffer[Array[(Int, Int)]]()
    for (x <- 0 until w) {
      val column = new ArrayBuffer[(Int, Int)](h)
      for (y <- 0 until h) {
        column += ((0, 0))
      }      
      tileMap += column.toArray[(Int, Int)]
    }
    return tileMap.toArray     
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
    try {
      val map = parseMapFromXml(XML.loadFile(mapFileName))

      println("Loaded map " + mapFileName + "of size in tiles (" + map.wInTiles + "," + map.hInTiles + 
            "), in pixels ( " + map.w + "," + map.h + ") with " + map.layers.size + " layers and " + map.tiles.size + " tiles")
      return map
      
    } catch {
      case e:java.io.FileNotFoundException => println("Unknown map " + mapFileName)
    }
    
    return new DummyMap()
  }
  
  def parseMapFromXml(mapXml : Elem) : Map = {    
    val w = (mapXml \ "@width").text.toInt
    val h = (mapXml \ "@height").text.toInt
    val tileW = (mapXml \ "@tilewidth").text.toInt 
    val tileH = (mapXml \ "@tileheight").text.toInt
    val tileSets = parseTileSets(mapXml)
    val layers = parseLayers(mapXml)
    val tiles = createSingleTileMapFromManyTileSets(tileSets)
    return new Map(w, h, tileW, tileH, tileSets, layers, tiles)
  }

  
  def parseTileSets(mapXml:Elem) : Array[TileSet] = {
    val tileSets = new ArrayBuffer[TileSet]()
    val tileSetElem = mapXml \\ "tileset"
    
    tileSetElem.foreach { tileSetNode => 
      val tileSet = TileSet(tileSetNode)
      tileSets += tileSet
    }
    return tileSets.toArray[TileSet]
  }

  def parseLayers(mapXml : Elem) : Array[Layer] = {
    val layers = new ArrayBuffer[Layer]()
    
    val layerElem = mapXml \\ "layer"
    
    layerElem.foreach { layerNode => 
      val layer = Layer(layerNode)
      layers += layer
    }
    return layers.toArray[Layer]
  }
  
  def createSingleTileMapFromManyTileSets(tileSets:Array[TileSet]) : Array[Tile] = {
    tileSets.sortWith((l:TileSet, r:TileSet)=>{l.firstTileIndex > r.firstTileIndex})
    val tiles = new ArrayBuffer[Tile](0)
    tileSets.foreach(tileSet => tiles ++= tileSet.tiles)
    return tiles.toArray[Tile]
  }
}

