package grambers

import junit.framework._
import Assert._
import net.iharder.Base64

class MapTest extends TestCase {

  def testWorldPointToTilePoint {
    val map = new Map
    assertEquals((0, 0), map.worldPointToTileIndex(Point(0, 0)))
    assertEquals((1, 1), map.worldPointToTileIndex(Point(map.tileW, map.tileH)))
    assertEquals((0, 1), map.worldPointToTileIndex(Point(map.tileW/2, map.tileH)))
    assertEquals((1, 0), map.worldPointToTileIndex(Point(map.tileW, map.tileH/2)))
  }
      
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
     
     // Warning: causes a file read to the tile sets referenced in the parsed XML! 
     def testParseTileSetsFromMapXml {
       val tileSets = MapLoader.parseTileSets(testMapXml)
       assertEquals(2, tileSets.size)
       assertEquals(32, tileSets(0).tileW)
       assertEquals(32, tileSets(0).tileH)
       assertEquals(6, tileSets(0).h)
       assertEquals(tileSets(0).w*tileSets(0).h, tileSets(0).tiles.size)
     }
}
