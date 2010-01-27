package grambers

import java.lang.Math._
import java.awt.Color

// Playfield class, used to demo the gaming library
object Main {
        
    def bigBangTwoWallsAndABall(universe : Universe) : Universe = {      
      val yellowRoundThing = new grambers.RoundThing(Point(150, 50), 10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 10.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redBox = new grambers.Box(20, 80);
      redBox.color = java.awt.Color.red
      redBox.speed = 0
      redBox.direction = 180
      redBox.center = new Point(100, 50)
      redBox.mass = 10000.0
      redBox.doYourThing = (redBox) => {redBox.turn(0)}

      val blueBox = new grambers.Box(20, 100);
      blueBox.color = java.awt.Color.blue
      blueBox.speed = 0
      blueBox.direction = 180
      blueBox.center = new Point(200, 50)
      blueBox.mass = 10000.0
      blueBox.doYourThing = (redBox) => {blueBox.turn(0)}
      
      universe.things += redBox
      universe.things += blueBox
      universe.things += yellowRoundThing
      
      return universe
    }  

    def bigBangEdgesAndStuff(universe : Universe) : Universe = {
      val universe = new Universe(600, 300);
      
      val yellowRoundThing = new grambers.RoundThing(Point(150, 50), 10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 10.0
      yellowRoundThing.direction = 30.0
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.accelerate(0.001)}
      

      val blueBox = new grambers.Box(20, 50);
      blueBox.color = java.awt.Color.blue
      blueBox.center = new Point(200, 50)
      blueBox.mass = 10000.0
      blueBox.doYourThing = (redBox) => {blueBox.turn(0)}
      
      universe.things += blueBox
      universe.things += yellowRoundThing

      addWalls(universe)      
      return universe
    }  
    
    def addWalls(universe : Universe) {
      val box = new grambers.Box(universe.WIDTH, 1);
      box.color = java.awt.Color.black
      box.center = new Point(universe.WIDTH/2, 10)
      box.mass = 1000000.0
      universe.things += box
      
      val box2 = new grambers.Box(universe.WIDTH, 1);
      box2.color = java.awt.Color.black
      box2.center = new Point(universe.WIDTH/2, universe.HEIGHT-10)
      box2.mass = 1000000.0
      universe.things += box2
      
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
      var ball = RoundThing(10, 30, 10, Color.yellow, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
 
      ball = RoundThing(100, 100, 5, Color.red, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})
      
      ball = RoundThing(20, 150, 40, Color.black, random()*10, random()*360)
      addBall(universe, ball, (ball) => {ball.accelerate(0.001)})    
      
      val blueBox = new grambers.Box(20, 50);
      blueBox.color = java.awt.Color.blue
      blueBox.center = new Point(200, 50)
      blueBox.mass = 10000.0
      blueBox.doYourThing = (redBox) => {blueBox.turn(0)}     
      universe.things += blueBox
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
