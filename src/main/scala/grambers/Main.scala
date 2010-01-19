package grambers

// Playfield class, used to demo the gaming library
object Main {
    
    def bigBang2BallsHeadOn() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.location = (50, 100)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 1.0
      redRoundThing.direction = 180.0
      redRoundThing.location = (100, 100)
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }

    def bigBangTwoBalls45Angle() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 45.0
      yellowRoundThing.location = (50, 50)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 1.0
      redRoundThing.direction = 135
      redRoundThing.location = (100, 50)
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }

    def bigBangMovingAndStaticBall() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.location = (50, 50)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 2.0
      redRoundThing.direction = 180
      redRoundThing.location = (100, 50)
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }  
    
    def bigBangTwoWallsAndABall() : Universe = {
      val universe = new Universe(600, 300);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 10.0
      yellowRoundThing.location = (150, 50)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redBox = new grambers.Box(20, 80);
      redBox.color = java.awt.Color.red
      redBox.speed = 0
      redBox.direction = 180
      redBox.location = (100, 50)
      redBox.mass = 10000.0
      redBox.doYourThing = (redBox) => {redBox.turn(0)}

      val blueBox = new grambers.Box(20, 100);
      blueBox.color = java.awt.Color.blue
      blueBox.speed = 0
      blueBox.direction = 180
      blueBox.location = (200, 50)
      blueBox.mass = 10000.0
      blueBox.doYourThing = (redBox) => {blueBox.turn(0)}
      
      universe.things += redBox
      universe.things += blueBox
      universe.things += yellowRoundThing
      
      return universe
    }  
    
    def main(args:Array[String]) {
      val universe = args(0) match {
        case "2" => bigBang2BallsHeadOn
        case "1" => bigBangTwoWallsAndABall
        case "0" => bigBangTwoBalls45Angle
        case _ => bigBangMovingAndStaticBall
      }
      
      val observer = new Observer(universe, 500, 200)
      observer.observe()
    }
}
