package grambers

import java.lang.Math._
import java.awt.Color

// Playfield class, used to demo the gaming library
object Main {
    
    def bigBangTwoWallsAndABall(universe : Universe) {      

      var ball = RoundThing(150, 50, 10, 10, Color.yellow, 1, 0)
      addBall(universe, ball, (ball) => {
          ball.accelerate(0.001)
          ball.turn(-0.001)
        }
      )
      
      universe.addThing(Box(100, 50, 20, 80, Color.red))
      universe.addThing(Box(200, 50, 20, 100, Color.blue))
            
      val observer = new Observer(universe)
      observer.observe()
    }  

    def bigBangEdgesAndStuff(universe : Universe) {
      val universe = new Universe(600, 300);

      var ball = RoundThing(150, 50, 10, 10, Color.yellow, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
            
      universe.addThing(Box(200, 50, 20, 50, Color.blue))

      addWalls(universe)      
      val observer = new Observer(universe)
      observer.observe()
    }  
    
    def addWalls(universe : Universe) {
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

    def addBall(universe : Universe, ball : RoundThing, doYourThing : (Thing)=>Unit) {
      ball.doYourThing = doYourThing
      universe.addThing(ball)
    }
    
    def bigBangBallsAndWalls(universe : Universe) {
      addWalls(universe)
      var ball = RoundThing(100, 100, 10, 10, Color.yellow, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
 
      ball = RoundThing(500, 100, 5, 5, Color.red, random() * 100, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
      
      ball = RoundThing(50, 150, 40, 40, Color.black, random()*50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})    
      
      universe.addThing(Box(200, 50, 20, 50, Color.blue))
      
      val observer = new Observer(universe)
      //observer.w = 250
      //observer.h = 250
      observer.observe()
    }
    
    def main(args:Array[String]) {
      if (args.length > 0)
        args(0) match {
          case "2" => bigBangEdgesAndStuff(new Universe(600, 300))
          case "1" => bigBangTwoWallsAndABall(new Universe(600, 300))
          case _ => bigBangBallsAndWalls(new Universe(800, 400))
        }
      else 
        bigBangBallsAndWalls(new Universe(600, 300))
    }
}
