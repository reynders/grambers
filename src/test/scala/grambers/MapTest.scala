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
                    <objectgroup name="SOLIDS" width="40" height="20">
                      <object x="0" y="0" width="1280" height="32"/>
                      <object name="HOMEBASE" type="GAME_OBJECT" x="279" y="217" width="93" height="31">
                       <properties>
                        <property name="start_offset_x" value="50"/>
                        <property name="start_offset_y" value="15"/>
                       </properties>
                      </object>
                      <object x="806" y="217">
                       <polygon points="0,0 -62,62 62,62"/>
                      </object>
                      <object x="96" y="64">
                        <polyline points="0,0 0,384 736,384 736,0"/>
                      </object>
                      <object x="1248" y="32" width="32" height="608"/>
                    </objectgroup>
                    <objectgroup name="SOLIDS_2" width="40" height="20">
                      <object x="992" y="589" width="31" height="124"/>
                      <object x="961" y="651" width="31" height="62"/>
                    </objectgroup>
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

  def testParseMapObjectGroups {
    var mapObjectGroups = MapLoader.parseMapObjectGroups(testMapXml)
    assertEquals(2, mapObjectGroups.size)
    assertEquals("SOLIDS", mapObjectGroups(0).name)
    assertEquals("SOLIDS_2", mapObjectGroups(1).name)
  }

  def testParseMapObject {
    val solidsObjects = (testMapXml \\ "objectgroup")(0) \ "object"
    var mo = MapLoader.parseMapObject(solidsObjects(0)) // First object of SOLIDS

    assertEquals(0, mo.x); assertEquals(0, mo.y);
    assertEquals(1280, mo.w); assertEquals(32, mo.h);
    assertEquals("", mo.name); assertEquals("", mo.typeStr)
    assertEquals(0, mo.properties.size)
  }

  def testParseMapObjectWithProperties {
    val solidsObjects = (testMapXml \\ "objectgroup")(0) \ "object"
    val mo = MapLoader.parseMapObject(solidsObjects(1)) // Second object of SOLIDS

    assertEquals(279, mo.x); assertEquals(217, mo.y);
    assertEquals(93, mo.w); assertEquals(31, mo.h);
    assertEquals("HOMEBASE", mo.name); assertEquals("GAME_OBJECT", mo.typeStr)
    assertEquals(2, mo.properties.size)
    assertEquals("50", mo.properties("start_offset_x")); assertEquals("15", mo.properties("start_offset_y"))
  }

  def testParseMapObjectWithPolygonPoints {
    val solidsObjects = (testMapXml \\ "objectgroup")(0) \ "object"
    val mo = MapLoader.parseMapObject(solidsObjects(2)) // Third object of SOLIDS
    assertEquals(806, mo.x); assertEquals(217, mo.y);
    assertEquals(3, mo.polygonPoints.size)
    assertEquals(Point(0, 0), mo.polygonPoints(0)); assertEquals(Point(-62, 62), mo.polygonPoints(1));
  }

  def testParseMapObjectWithPolylinePoints {
    val solidsObjects = (testMapXml \\ "objectgroup")(0) \ "object"
    val mo = MapLoader.parseMapObject(solidsObjects(3)) // Fourth object of SOLIDS
    assertEquals(96, mo.x); assertEquals(64, mo.y);
    assertEquals(4, mo.polylinePoints.size)
    assertEquals(Point(0, 0), mo.polylinePoints(0)); assertEquals(Point(0, 384), mo.polylinePoints(1));
  }

  def testMapObjectsToThings {
    val solidsObjects = (testMapXml \\ "objectgroup")(0) \ "object"
    val mapObjects = MapLoader.parseMapObjects(solidsObjects)
    val things = MapLoader.mapObjectsToThings(mapObjects)
    assertEquals(things.size, mapObjects.size)
    assertTrue(things(0).isInstanceOf[RectangleStaticThing])
    assertTrue(things(1).isInstanceOf[RectangleStaticThing])
    assertTrue(things(2).isInstanceOf[PolygonStaticThing])
  }
}