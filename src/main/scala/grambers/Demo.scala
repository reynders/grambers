package grambers

import java.lang.Math._
import java.awt.Color
import java.lang.System._

import scala.collection.immutable.List

// Playfield class, used to demo the gaming library
object Demo {

    val WINDOW_W = 600
    val WINDOW_H = 500
    val TESTMAP = "resources/maps/testmap_40x20.tmx"
    val STARMAP = "resources/maps/starsky_100x100.tmx"
    val GAMEMAP = "resources/maps/map_1.tmx"
    val SHIP = "resources/gameobjects/ship_gf.xml"
    val TRIANGLE = "resources/gameobjects/triangle.xml"

    def createShip(position : Point) : Ship = new Ship(position, GameObject.load(SHIP))
  
    def createTriangle(position : Point) : GameObjectMovingThing =
        new GameObjectMovingThing(position, GameObject.load(TRIANGLE))

    def addBall(universe : Universe, ball : CircleMovingThing, doYourThing : (Thing)=>Unit) {
      ball.doYourThing = doYourThing
      universe.addThing(ball)
    }

    def twoBallsDemo {
      val universe = new Universe(TESTMAP, true)

      var ball = CircleMovingThing(200, 100, 10, Color.yellow)
      ball.setSpeedAndDirection(new Vector(-5, 10), 90)
      addBall(universe, ball, (ball) => {})

      ball = CircleMovingThing(150, 100, 20, Color.blue)
      ball.setSpeedAndDirection(new Vector(5, 10), 90)
      addBall(universe, ball, (ball) => {})

      ball = CircleMovingThing(320, 320, 32, Color.red)
      ball.setSpeedAndDirection(new Vector(0, 0), 90)
      addBall(universe, ball, (ball) => {})

      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ball)

      universe.run(observer)
    }

    def shipDemo {
      val universe = new Universe(TESTMAP, true)
      val ship = createShip(Point(320, 320))
      universe.addThing(ship)

      val ball = CircleMovingThing(320, 370, 32, Color.red)
      addBall(universe, ball, (ball) => {})

      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ship)
      observer.WindowToWorld.addKeyListener(ShipKeyboardController(ship))
      universe.run(observer)
    }

    def triangleDemo {
      //org.jbox2d.common.Settings.maxPolygonVertices = 13
      val universe = new Universe(TESTMAP, true)
      val ship1 = createTriangle(Point(320, 320))
      // ship1.setSpeedAndDirection(new Vector(5, 10), 90)
      universe.addThing(ship1)

      val ball = CircleMovingThing(320, 380, 16, Color.red)
      addBall(universe, ball, (ball) => {})

      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ship1)
      universe.run(observer)
    }

    def gameDemo {
      val universe = new Universe(GAMEMAP, true)
      val ship = createShip(Point(320, 320))
      universe.addThing(ship)

      val ball = CircleMovingThing(320, 370, 32, Color.red)
      addBall(universe, ball, (ball) => {})

      val observer = new Observer(WINDOW_W, WINDOW_H, universe, ship)
      observer.WindowToWorld.addKeyListener(ShipKeyboardController(ship))
      universe.run(observer)  
    }

    def main(args:Array[String]) {
      if (args.length > 0)
        args(0) match {
          case "B" => twoBallsDemo
          case "S" => shipDemo
          case "T" => triangleDemo
          case "G" => gameDemo
//          case "3" => ballsAndWallsAndCameraDemo
//          case "2" => edgesAndStuffDemo
//          case "1" => twoWallsAndABallDemo
          case _ => twoBallsDemo
        }
      else
        twoBallsDemo
    }
}
