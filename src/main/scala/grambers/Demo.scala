package grambers

import java.lang.Math._
import java.awt.Color
import java.lang.System._

import scala.collection.immutable.List

// Playfield class, used to demo the gaming library
object Demo {
  
    val WINDOW_W = 600
    val WINDOW_H = 300
    val TESTMAP = "resources/maps/testmap_40x20.tmx"  
    val STARMAP = "resources/maps/starsky_100x100.tmx"  
  
/*    
    def twoWallsAndABallDemo {      

      val universe = new Universe(TESTMAP)
      
      var ball = RoundThing(150, 50, 10, 10, Color.yellow, 1, 0)
      addBall(universe, ball, (ball) => {
          ball.accelerate(0.001)
          ball.turn(-0.001)
        }
      )
      
      universe.addThing(Box(100, 50, 20, 80, Color.red))
      universe.addThing(Box(200, 50, 20, 100, Color.blue))
            
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)
      universe.run(observer)
    }  

    def edgesAndStuffDemo {
      val universe = new Universe(TESTMAP);

      var ball = RoundThing(150, 50, 10, 10, Color.yellow, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
            
      universe.addThing(Box(200, 50, 20, 50, Color.blue))

      addWalls(universe)      
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)
      universe.run(observer)
    }  

    def addWalls(universe : Universe) {
      println("addWalls: (" + universe.WIDTH + "," + universe.HEIGHT + ")")
      universe.addThing(new Wall(Point(0,0), Point(universe.WIDTH-1, 0)))
      universe.addThing(new Wall(Point(0,0), Point(0, universe.HEIGHT-1)))
      universe.addThing(new Wall(Point(universe.WIDTH-1,0), Point(universe.WIDTH-1, universe.HEIGHT-1)))
      universe.addThing(new Wall(Point(0, universe.HEIGHT-1), Point(universe.WIDTH-1, universe.HEIGHT-1)))
    }

    def addWalls2(universe : Universe) {
      println("addWalls: (" + universe.WIDTH + "," + universe.HEIGHT + ")")
      universe.addThing(Box(universe.WIDTH/2, 5, universe.WIDTH, 1, Color.blue))
      universe.addThing(Box(universe.WIDTH/2, universe.HEIGHT-5, universe.WIDTH, 1, Color.blue))    
      universe.addThing(Box(5, universe.HEIGHT/2, 1, universe.HEIGHT, Color.blue))
      universe.addThing(Box(universe.WIDTH-5, universe.HEIGHT/2, 1, universe.HEIGHT, Color.blue))
    }
    
    def addRandomBall(universe : Universe) {
      val startX = random * universe.WIDTH
      val startY = random * universe.HEIGHT      
      val ball = new grambers.RoundThing(Point(startX, startY), random * 20);
      ball.color = java.awt.Color.yellow
      ball.speed = random * 20
      ball.direction = random * 360
      ball.doYourThing = (ball) => {ball.accelerate(0.001)}
      universe.addThing(ball)
    }

    def ballsAndWallsAndCameraDemo {
      val universe = new Universe(TESTMAP)
      addWalls(universe)
      var ball = RoundThing(200, 100, 10, 10, Color.yellow, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
 
      ball = RoundThing(500, 100, 5, 5, Color.red, random() * 100, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
      
      ball = RoundThing(50, 150, 40, 40, Color.black, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})    
      
      universe.addThing(Box(200, 50, 20, 50, Color.blue))
      
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)
      observer.camera = createBallFollowingDemoCamera(ball)
      observer.w = 400
      observer.h = 200
      observer.position = Point(universe.WIDTH/2, universe.HEIGHT/2)
      universe.run(observer)
    }

    def createBallFollowingDemoCamera(ball : RoundThing) : Camera = new Camera {
    
      override def move(observer : Observer) {
        observer.position = ball.center
      }
    }

    
    def createCollidingDemoCamera : Camera = new Camera {
      var xDir = 1
      var yDir = 1
      var lastUpdate = currentTimeMillis
    
      override def move(observer : Observer) {
        if (currentTimeMillis - lastUpdate > 500) {
          if ((observer.position.x + (observer.w/2) >= observer.universe.WIDTH) ||
              (observer.position.x - (observer.w/2) <= 0))
            xDir *= -1
          if ((observer.position.y + (observer.h/2) >= observer.universe.HEIGHT) ||
              (observer.position.y - (observer.h/2) <= 0))
            yDir *= -1   
    
          //println("Setting position from " + observer.position + " to " + Point(observer.position.x + xDir, observer.position.y + yDir) )
          observer.position = Point(observer.position.x + xDir, observer.position.y + yDir)  
          lastUpdate = currentTimeMillis
        }
      }
    }
    
    def ballsAndWallsDemo {
      val universe = new Universe(TESTMAP)
      addWalls(universe)
      var ball = RoundThing(200, 100, 10, 10, Color.yellow, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
 
      ball = RoundThing(500, 100, 5, 5, Color.red, random() * 100, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
      
      ball = RoundThing(50, 150, 40, 40, Color.black, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})    
      
      universe.addThing(Box(200, 50, 20, 50, Color.blue))
      
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)

      universe.run(observer)
    }
    
    def imageDemo {
      val universe = new Universe(TESTMAP)
      addWalls(universe)
      
      val ball = ImageRoundThing(100, 100, 23, 23, "resources/gfx/ball_50x50.gif", random() * 100, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.005)})
      
      universe.run(new Observer(WINDOW_W, WINDOW_H, universe, ball))
    }

    def debugDemo {
      val universe = new Universe(TESTMAP)
      addWalls(universe)
      
      val ball = ImageRoundThing(100, 100, 23, 23, "resources/gfx/ball_50x50.gif", 10, 0)
      addBall(universe, ball, (ball) => {})
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)
      //observer.camera = new Camera {      
      //  override def move(observer : Observer) {}
      //}
      
      universe.run(observer)
    }

    def starDemo {
      val universe = new Universe(STARMAP)
      addWalls(universe)
      
      val ball = ImageRoundThing(100, 100, 45, 57, "resources/gfx/ship.gif", 10, 270)
      addBall(universe, ball, (ball) => {})
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)
      
      universe.run(observer)
    }

*/
    def createShip(position : Point) : PolygonThing = {
      new PolygonThing(position, 10, "resources/gfx/ship.gif",
              List[(Int, Int)]((31, 10), (117, 56), (31, 102), (0, 66), (0, 43)))
               //List[(Int, Int)]((0, 28), (22, 0), (36, 0), (74,25), (98, 25), (98, 33), (113, 43), (97, 54), (97, 60), (75, 60), (34, 85), (22, 85), (0, 54)))
    }
    
    def addBall(universe : Universe, ball : CircleThing, doYourThing : (Thing)=>Unit) {
      ball.doYourThing = doYourThing
      universe.addThing(ball)
    }
    
    def twoBallsDemo {
      val universe = new Universe(TESTMAP, true)

      var ball = CircleThing(200, 100, 10, Color.yellow)
      ball.setSpeedAndDirection(new Vector(-5, 10), 90)      
      addBall(universe, ball, (ball) => {})
      
      ball = CircleThing(150, 100, 20, Color.blue)
      ball.setSpeedAndDirection(new Vector(5, 10), 90) 
      addBall(universe, ball, (ball) => {})
      
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)

      universe.run(observer) 
    }
    
    def shipDemo {
      org.jbox2d.common.Settings.maxPolygonVertices = 13
      val universe = new Universe(STARMAP, true)
      val ship1 = createShip(Point(100, 50))
      ship1.setSpeedAndDirection(new Vector(5, 10), 90) 
      universe.addThing(ship1)
      val ship2 = createShip(Point(250, 80))
      ship2.setSpeedAndDirection(new Vector(-5, 10), 90) 
      universe.addThing(ship2)
      
      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ship1)
      Config.debugDrawShapes = true
      universe.run(observer)
    }

    def main(args:Array[String]) {
      if (args.length > 0)
        args(0) match {          
          case "B" => twoBallsDemo
          case "S" => shipDemo
//          case "D" => debugDemo
//          case "I" => imageDemo
//          case "3" => ballsAndWallsAndCameraDemo
//          case "2" => edgesAndStuffDemo
//          case "1" => twoWallsAndABallDemo          
          case _ => twoBallsDemo
        }
      else 
        twoBallsDemo
    }
}
