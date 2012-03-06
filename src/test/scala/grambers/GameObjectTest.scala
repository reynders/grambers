package grambers

import junit.framework._
import Assert._

class GameObjectTest extends TestCase {

  val gameObjectXml = <gameobject name="ship_gf" type="moving">
                         <gfx file="resources/gfx/ships.gif" animated="true" w="25" h="20"
                              rows="1" columns="3" x_offset="0" y_offset="0"
                              rotates="true" rotation_count="10"/>
                      </gameobject>

  def testSpriteLoader {
    val sprite = SpriteLoader.load("resources/gfx/ships.gif", 25, 20, 1, 3, 0, 0, false, 0)
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertFalse(sprite.rotates)
    assertEquals(0, sprite.rotationCount)
  }

  def testParseSprite {
    val sprite = SpriteLoader.parseSprite(gameObjectXml)
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertTrue(sprite.rotates)
    assertEquals(10, sprite.rotationCount)
  }
}