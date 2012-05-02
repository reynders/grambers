package grambers

import junit.framework._
import Assert._

class SpriteTest extends TestCase {

  val spriteXml = <gfx file="resources/gfx/ships.gif" animated="true" w="25" h="20"
                         rows="1" columns="3" x_offset="0" y_offset="0"
                         action="thrust" animation_fps="180"
                         rotates="true" rotation_count="10" />

  def testSpriteCreation {
    val sprite = new Sprite("resources/gfx/ships.gif", 25, 20, 1, 3, 0, 0, "testKey", 360, false, 0)
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
    val sprite = GameObject.parseSprite(spriteXml)
    assertEquals("resources/gfx/ships.gif", sprite.name)
    assertEquals(75, sprite.imgW)
    assertEquals(20, sprite.imgH)
    assertEquals(3, sprite.images.size)
    assertEquals("thrust", sprite.action)
    assertEquals(180, sprite.animationFps)
    assertTrue(sprite.rotates)
    assertEquals(10, sprite.rotationCount)
  }

  def testParseMassBody {
    var massBody = GameObject.parseMassBody(<mass_body type="circle" center="0,6" r="3" />)
    assertTrue(massBody.isInstanceOf[CircleMassBody])
    val cmb = massBody.asInstanceOf[CircleMassBody]
    assertEquals(Point(0,6), cmb.c)
    assertEquals(3.0, cmb.r)

    massBody = GameObject.parseMassBody(<mass_body type="rectangle" center="-8,-4" w="5" h="4" />)
    assertTrue(massBody.isInstanceOf[RectangleMassBody])
    val rmb = massBody.asInstanceOf[RectangleMassBody]
    assertEquals(Point(-8,-4), rmb.c)
    assertEquals(5.0, rmb.w)
    assertEquals(4.0, rmb.h)

    massBody = GameObject.parseMassBody(<mass_body type="polygon" center="1,-1" points="13,-12 13,13 -12,-12" />)
    assertTrue(massBody.isInstanceOf[PolygonMassBody])
    val pmb = massBody.asInstanceOf[PolygonMassBody]
    assertEquals(Point(1,-1), pmb.c)
    assertEquals(3, pmb.points.size)
    assertEquals(Point(-12, -12), pmb.points(2))
  }

  def testParseForce {
    val force = GameObject.parseForce(<force force_vector="0,50" application_point="12,-13" action="LEFT" />)
    assertTrue(force.isInstanceOf[Force])
    assertEquals(Point(0, 50), force.forceVector)
    assertEquals(Point(12, -13), force.applicationPoint)
    assertEquals("LEFT", force.action)

    val forces = GameObject.parseForces(<force force_vector="0,50" application_point="12,-13" action="LEFT" />
                                          <force force_vector="0,50" application_point="-12,-13" action="RIGHT" />
                                          <force force_vector="0,500" application_point="0,0" action="UP" />
                                          <force force_vector="0,-500" application_point="0,0" action="DOWN" />)
    assertEquals(4, forces.size)
    assertEquals(Point(0, -500), forces(3).forceVector)
  }
}