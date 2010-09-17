package grambers

import junit.framework._
import Assert._
import net.iharder.Base64

class MapTest extends TestCase {
     
  val testMapXml = <map version="1.0" orientation="orthogonal" width="4" height="3" tilewidth="32" tileheight="32">
                    <tileset firstgid="1" name="tileset0" tilewidth="32" tileheight="32" spacing="1">
                      <image source="resources/gfx/testtileset_8x6.png"/>
                    </tileset>
                    <tileset firstgid="37" name="tileset1" tilewidth="32" tileheight="32" spacing="1">
                      <image source="resources/gfx/testtileset_6x6.png"/>
                    </tileset>
                    <layer name="Layer 1" width="4" height="3">
                      <data encoding="base64" compression="gzip">
                        H4sIAAAAAAAAC1NlYGBggmJ1IGZE4oPYpkh8cyAGAPAUonAwAAAA
                      </data>
                    </layer>
                  </map>

  // Warning: causes a file read to the tile sets referenced in the parsed XML!     
  val map = MapLoader.parseMapFromXml(testMapXml)    
  
  def testWorldPointToTilePoint {    
      assertEquals((0, 0), map.worldPointToTileIndex(Point(0, 0)))
      assertEquals((0, 0), map.worldPointToTileIndex(Point(-1, -1)))
      assertEquals((1, 1), map.worldPointToTileIndex(Point(32, 32)))
      assertEquals((0, 1), map.worldPointToTileIndex(Point(5, 32)))
      assertEquals((1, 0), map.worldPointToTileIndex(Point(32, 5)))
      assertEquals((3, 2), map.worldPointToTileIndex(Point(32*3, 32*2)))
  }

  def testCreateBackgroundImageFromTiles {
    var bg = map.createBackgroundImageFromTiles((0,0), (1,1))
    var expectedW = (map.TILE_BUFFER_PADDING_TILES + 2) * map.tileW 
    assertEquals(expectedW, bg.image.getWidth)
    var expectedH = (map.TILE_BUFFER_PADDING_TILES + 1) * map.tileH // The method under testing limits the height to the max height of the test map which is 3
    assertEquals(expectedH, bg.image.getHeight) 
    assertEquals(Rectangle(0, 0, expectedW-1, expectedH-1), bg.worldCoordinates)

    bg = map.createBackgroundImageFromTiles((3,2), (3,2)) // Just a single tile, the rlp of 4,3 size test map
    expectedW = (map.TILE_BUFFER_PADDING_TILES + 1) * map.tileW 
    assertEquals(expectedW, bg.image.getWidth)
    expectedH = (map.TILE_BUFFER_PADDING_TILES + 1) * map.tileH
    assertEquals(expectedH, bg.image.getHeight) 
    assertEquals(Rectangle(32, 0, 32+(map.tileW*3)-1, (map.tileH*3)-1), bg.worldCoordinates)
  }

              
     def testParseLayersFromMapXml {
       val layers = MapLoader.parseLayers(testMapXml)
       assertEquals(1, layers.size)
     }
     
     def testLayerCreateInitialTileMap {
       val map = Layer.createInitialTileMap(5, 5)
       assertEquals((0,0), map(4)(4)) 
     }
     
     def testParseLayerDataFromMapXml {
       val tileMap = Layer.parseLayerData("H4sIAAAAAAAAC1NlYGBggmJ1IGZE4oPYpkh8cyAGAPAUonAwAAAA", 4, 3)
       assertEquals(4, tileMap.size)
       assertEquals(3, tileMap(0).size)
       assertEquals(37, tileMap(0)(0)._2)
       assertEquals(55, tileMap(3)(2)._2)
     }
    
   def testParseTileSetsFromMapXml {
     val tileSets = MapLoader.parseTileSets(testMapXml)
     assertEquals(2, tileSets.size)
     assertEquals(32, tileSets(0).tileW)
     assertEquals(32, tileSets(0).tileH)
     assertEquals(6, tileSets(0).h)
     assertEquals(tileSets(0).w*tileSets(0).h, tileSets(0).tiles.size)
   }
     
   def testCreateSingleTileMapFromManyTileSets = {
     var tileSets = MapLoader.parseTileSets(testMapXml)
     assertEquals(2, tileSets.size)
     assertEquals(((8*6)+(6*6)), tileSets(0).tiles.size + tileSets(1).tiles.size)
     if (tileSets(0).firstTileIndex != 0) tileSets = tileSets.reverse
     val tiles = MapLoader.createSingleTileMapFromManyTileSets(tileSets)
     assertEquals(((8*6)+(6*6)), tiles.size)
     assertEquals(tiles(0), tileSets(0).tiles(0)) 
   }
}
