package grambers

import junit.framework._
import Assert._

class SpriteTest extends TestCase {

  val spriteXml = <gameobject name="ship_gf" type="moving">
                    <gfx file="resources/gfx/ships.gif" animated="true" w="25" h="20"
                         rows="1" columns="3" x_offset="0" y_offset="0"
                         action="thrust" animation_fps="180"
                         rotates="true" rotation_count="10">
                      <mass_body type="polygon" center="0,0" points="2,2, -1,1 0,0"/>
                    </gfx>
                  </gameobject>

  def testSpriteLoader {
    val sprite = SpriteLoader.load("resources/gfx/ships.gif", 25, 20, 1, 3, 0, 0, "testKey", 360, false, 0, new Array[MassBody](0), new Array[Force](0))
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertEquals("testKey", sprite.action)
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
    assertEquals("thrust", sprite.action)
    assertEquals(180, sprite.animationFps)
    assertTrue(sprite.rotates)
    assertEquals(10, sprite.rotationCount)
    assertEquals(1, sprite.massBodies.size)
    val pmb = sprite.massBodies(0).asInstanceOf[PolygonMassBody]
    assertEquals(3, pmb.points.size)
    assertEquals(Point(2,2), pmb.points(0))
    assertEquals(Point(-1,1), pmb.points(1))
    assertEquals(Point(0,0), pmb.points(2))
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
    assertEquals((0, 0), sprite.getCurrentImageIndex(-360, false, now))
    assertEquals((0, 9), sprite.getCurrentImageIndex(-1, false, now))
  }

  def testCurrentAnimationIndex {
    val sprite = SpriteLoader.parseSprite(spriteXml)
    assertEquals(0, sprite.currentAnimationFrameIndex(0))
    assertEquals(1, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs))
    assertEquals(2, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs*2))
    assertEquals(0, sprite.currentAnimationFrameIndex(sprite.animationDtBetweenFramesInMs*3))
  }

  def testParseMassBody {
    var massBody = SpriteLoader.parseMassBody(<mass_body type="circle" center="0,6" r="3" />)
    assertTrue(massBody.isInstanceOf[CircleMassBody])
    val cmb = massBody.asInstanceOf[CircleMassBody]
    assertEquals(Point(0,6), cmb.c)
    assertEquals(3.0, cmb.r)

    massBody = SpriteLoader.parseMassBody(<mass_body type="rectangle" center="-8,-4" w="5" h="4" />)
    assertTrue(massBody.isInstanceOf[RectangleMassBody])
    val rmb = massBody.asInstanceOf[RectangleMassBody]
    assertEquals(Point(-8,-4), rmb.c)
    assertEquals(5.0, rmb.w)
    assertEquals(4.0, rmb.h)

    massBody = SpriteLoader.parseMassBody(<mass_body type="polygon" center="1,-1" points="13,-12 13,13 -12,-12" />)
    assertTrue(massBody.isInstanceOf[PolygonMassBody])
    val pmb = massBody.asInstanceOf[PolygonMassBody]
    assertEquals(Point(1,-1), pmb.c)
    assertEquals(3, pmb.points.size)
    assertEquals(Point(-12, -12), pmb.points(2))
  }

  def testParseForce {
    val force = SpriteLoader.parseForce(<force force_vector="0,50" application_point="12,-13" action="LEFT" />)
    assertTrue(force.isInstanceOf[Force])
    assertEquals(Point(0, 50), force.forceVector)
    assertEquals(Point(12, -13), force.applicationPoint)
    assertEquals("LEFT", force.action)
    
    val forces = SpriteLoader.parseForces(<force force_vector="0,50" application_point="12,-13" action="LEFT" />
                                          <force force_vector="0,50" application_point="-12,-13" action="RIGHT" />
                                          <force force_vector="0,500" application_point="0,0" action="UP" />
                                          <force force_vector="0,-500" application_point="0,0" action="DOWN" />)
    assertEquals(4, forces.size)
    assertEquals(Point(0, -500), forces(3).forceVector)
  }
}