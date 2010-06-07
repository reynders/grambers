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
  
  def testBase64Decode {
      val byteArray : Array[Byte] = Base64.decode("dGVzdA==") 
      assertEquals("test", new String(byteArray))
    }
    
    def testGZipDecode {
      val encoded = Base64.encodeBytes("testgzip".getBytes(), Base64.GZIP);     
      assertTrue(encoded != null)
      assertTrue(!encoded.equals("testgzip"))
      assertEquals("testgzip", new String(Base64.decode(encoded)))
    }
    
    def testTiledDecode {
      val byteArray : Array[Byte] = Base64.decode("H4sIAAAAAAAAC2NkYGBggmJGKMbFZwZiAJN2z9cwAAAA")
      for (i <- 0 until byteArray.size) {
        if (i%4 == 0)
          println(i + ":" + BigInt(byteArray.slice(i, i+4).reverse))
      }
      assertTrue(1==1)
    }
    
    def testFileLoad = {
      import scala.xml._
      //val file = scala.io.Source.fromFile(new java.io.File("resources/maps/test.tmx")).mkString
      val xml = Utility.toXML(XML.loadFile("resources/maps/test.tmx"))
      println("Xml: " + xml)
      
      assertTrue(xml.startsWith("<map"))
    }
    
    val testMapXml = <map version="1.0" orientation="orthogonal" width="4" height="3" tilewidth="32" tileheight="32">
                    <tileset firstgid="1" name="tileset0" tilewidth="32" tileheight="32">
                      <image source="tile/examples/sewer_tileset.png"/>
                    </tileset>
                    <tileset firstgid="37" name="tileset1" tilewidth="32" tileheight="32">
                      <image source="tile/examples/tmw_desert_spacing.png"/>
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
}
