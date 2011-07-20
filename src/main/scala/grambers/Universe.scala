package grambers

import scala.collection.mutable._
import java.lang.Math._

import org.jbox2d.dynamics._
import org.jbox2d.common._
import org.jbox2d.collision.shapes._

class Universe(mapName:String, withWalls : Boolean) {
    
  var staticThings : Array[StaticThing] = new Array[StaticThing](0)
  var movingThings : Array[MovingThing] = new Array[MovingThing](0)
  
  val map : Map = MapLoader.loadMap(mapName)
  val WIDTH = map.w
  val HEIGHT = map.h
  var millisecondsSinceBigBang = 0; 
  
  if (withWalls) {
    val bd = new BodyDef()
    bd.position.set(0, 0)
    val ground = Universe.world.createBody(bd)
    val shape = new PolygonShape()
    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 0.0f
    fixtureDef.restitution = 0.4f
                        
    // Left vertical
    shape.setAsEdge(new Vec2(0, 0), new Vec2(0, HEIGHT));
    ground.createFixture(fixtureDef);
                        
    // Right vertical
    shape.setAsEdge(new Vec2(WIDTH, 0), new Vec2(WIDTH, HEIGHT));
    ground.createFixture(fixtureDef);
                        
    // Top horizontal
    shape.setAsEdge(new Vec2(0, 0), new Vec2(WIDTH, 0));
    ground.createFixture(fixtureDef);
                        
    // Bottom horizontal
    shape.setAsEdge(new Vec2(0, HEIGHT), new Vec2(WIDTH, HEIGHT));
    ground.createFixture(fixtureDef);
  }
  
  def addThing(thing : Thing) {
    thing match {
      case movingThing : MovingThing => movingThings :+= movingThing
      case staticThing : StaticThing => staticThings :+= staticThing
      case _ => println("addThing does not know what to do with " + thing)
    }
  }
    
  def run(observer : Observer) {      
    
    var now = Config.currentTimeMillis
    var nextWorldUpdateTime = now
    var lastWorldUpdateTime = now 

    while (true) {    
      
      now = Config.currentTimeMillis
      
      if (now >= nextWorldUpdateTime) {        
        Universe.world.step(Config.worldUpdateDt, Config.velocityIterations, Config.positionIterations)
        Universe.world.clearForces()
        nextWorldUpdateTime = now + (Config.worldUpdateDt * 1000).toLong        
        Config.worldUpdates += 1
      }
      
      lastWorldUpdateTime = Config.currentTimeMillis
      
      observer.observe()
      
      if (Config.measurementSamplePeriodMs > 1000) {
        Config.showAnimationStatistics
        Config.resetAnimationMeasurements
      }
    }
  }   
}
    
object Universe {
  val world = new World(new Vec2(0, -10), true)
}
