package grambers

import junit.framework._
import Assert._
import net.iharder.Base64

class MapTest extends TestCase {

  def testWorldPointToTilePoint {
    val map = new Map
    assertEquals(Point(0, 0), map.worldPointToTileIndex(Point(0, 0)))
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
}
