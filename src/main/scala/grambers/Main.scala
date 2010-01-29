package grambers

import java.lang.Math._
import java.awt.Color

// Playfield class, used to demo the gaming library
object Main {
        
    def bigBangTwoWallsAndABall(universe : Universe) : Universe = {      

      var ball = RoundThing(150, 50, 10, 10, Color.yellow, 1, 0)
      addBall(universe, ball, (ball) => {
          ball.accelerate(0.001)
          ball.turn(-0.001)
        }
      )
      
      universe.things += Box(100, 50, 20, 80, Color.red)
      universe.things += Box(200, 50, 20, 100, Color.blue)
            
      return universe
    }  

    def bigBangEdgesAndStuff(universe : Universe) : Universe = {
      val universe = new Universe(600, 300);

      var ball = RoundThing(150, 50, 10, 10, Color.yellow, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
            
      universe.things += Box(200, 50, 20, 50, Color.blue)

      addWalls(universe)      
      return universe
    }  
    
    def addWalls(universe : Universe) {
      universe.things += Box(universe.WIDTH/2, 10, universe.WIDTH, 1, Color.blue)
      universe.things += Box(universe.WIDTH/2, universe.HEIGHT-10, universe.WIDTH, 1, Color.blue)      
    }
    
    def addRandomBall(universe : Universe) {
      val startX = random * universe.WIDTH
      val startY = random * universe.HEIGHT      
      val ball = new grambers.RoundThing(Point(startX, startY), random * 20);
      ball.color = java.awt.Color.yellow
      ball.speed = random * 20
      ball.direction = random * 360
      ball.doYourThing = (ball) => {ball.accelerate(0.001)}
    }

    def addBall(universe : Universe, ball : RoundThing, doYourThing : (Thing)=>Unit) {
      ball.doYourThing = doYourThing
      universe.things += ball
    }
    
    def bigBangBallsAndWalls(universe : Universe) : Universe = {
      addWalls(universe)
      var ball = RoundThing(10, 30, 10, 10, Color.yellow, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
 
      ball = RoundThing(100, 100, 5, 5, Color.red, 50, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
      
      ball = RoundThing(20, 150, 40, 40, Color.black, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})    
      
      universe.things += Box(200, 50, 20, 50, Color.blue)
      return universe
    }
    
    def main(args:Array[String]) {
      var universe = if (args.length > 0)
        args(0) match {
          case "2" => bigBangEdgesAndStuff(new Universe(600, 300))
          case "1" => bigBangTwoWallsAndABall(new Universe(600, 300))
          case _ => bigBangBallsAndWalls(new Universe(600, 300))
        }
      else 
        bigBangBallsAndWalls(new Universe(600, 300))
      
      val observer = new Observer(universe)
      observer.observe()
    }
}
