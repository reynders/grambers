package grambers

import junit.framework._
import Assert._

class SpriteTest extends TestCase {

  val spriteXml = <gameobject name="ship_gf" type="moving">
                         <gfx file="resources/gfx/ships.gif" animated="true" w="25" h="20"
                              rows="1" columns="3" x_offset="0" y_offset="0"
                              animation_key="thrust" animation_fps="180"
                              rotates="true" rotation_count="10"
                              polygonPoints="0,0 -1,1 2,2"/>
                      </gameobject>

  def testSpriteLoader {
    val sprite = SpriteLoader.load("resources/gfx/ships.gif", 25, 20, 1, 3, 0, 0, "testKey", 360, false, 0, new Array[Point](0))
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertEquals("testKey", sprite.animationKey)
    assertEquals(360, sprite.animationFps)
    assertFalse(sprite.rotates)
    assertEquals(0, sprite.rotationCount)
  }

  def testParseSprite {
    val sprite = SpriteLoader.parseSprite(spriteXml)
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertEquals("thrust", sprite.animationKey)
    assertEquals(180, sprite.animationFps)
    assertTrue(sprite.rotates)
    assertEquals(10, sprite.rotationCount)
    assertEquals(3, sprite.polygonPoints.size)
    assertEquals(Point(0,0), sprite.polygonPoints(0))
    assertEquals(Point(-1,1), sprite.polygonPoints(1))
    assertEquals(Point(2,2), sprite.polygonPoints(2))
  }

  def testGetCurrentImageIndex() {
    val sprite = SpriteLoader.parseSprite(spriteXml)
    var now = Config.currentTimeMillis
    // Animation index tests
    assertEquals((0, 0), sprite.getCurrentImageIndex(0, false, now))
    now += sprite.animationDtBetweenFramesInMs
    assertEquals((0, 0), sprite.getCurrentImageIndex(0, false, now))

    // When animation just starts it should always return first fram
    now += sprite.animationDtBetweenFramesInMs
    assertEquals((0, 0), sprite.getCurrentImageIndex(0, true, now))

    now += sprite.animationDtBetweenFramesInMs
    assertEquals((1, 0), sprite.getCurrentImageIndex(0, true, now))

    now += sprite.animationDtBetweenFramesInMs
    assertEquals((2, 0), sprite.getCurrentImageIndex(0, true, now))

    // When animation stops it should return 0 frame always
    assertEquals((0, 0), sprite.getCurrentImageIndex(0, false, now))

    // Rotation index tests
    assertEquals((0, 0), sprite.getCurrentImageIndex(360, false, now))
    assertEquals((0, 0), sprite.getCurrentImageIndex(720, false, now))
    assertEquals((0, sprite.rotationCount / 2), sprite.getCurrentImageIndex(180, false, now))
    assertEquals((0, sprite.rotationCount / 2), sprite.getCurrentImageIndex(-180, false, now))
    assertEquals((0, 0), sprite.getCurrentImageIndex(-359, false, now))
  }

  def testCurrentAnimationIndex {
    val sprite = SpriteLoader.parseSprite(spriteXml)
    assertEquals(0, sprite.currentAnimationFrameIndex(0))
    assertEquals(1, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs))
    assertEquals(2, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs*2))
    assertEquals(0, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs*3))
  }
}