package grambers

// Playfield class, used to demo the gaming library
object Main {
    
    def bigBang2BallsHeadOn(universe : Universe) : Universe = {     
      val yellowRoundThing = new grambers.RoundThing(Point(100, 100), 10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(Point(130, 100), 10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 1.0
      redRoundThing.direction = 180.0
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }

    def bigBangTwoBalls45Angle(universe : Universe) : Universe = {     
      val yellowRoundThing = new grambers.RoundThing(Point(50, 50), 10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 45.0
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(Point(100, 50), 10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 1.0
      redRoundThing.direction = 135
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }

    def bigBangMovingAndStaticBall(universe : Universe) : Universe = {
      val yellowRoundThing = new grambers.RoundThing(Point(50, 50), 10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(Point(100, 50), 10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 2.0
      redRoundThing.direction = 180
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }  
    
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
      blueBox.speed = 0
      blueBox.direction = 180
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
    
    
    def main(args:Array[String]) {
      val universe = args(0) match {
        case "D" => bigBangEdgesAndStuff(new Universe(600, 300))
        case "2" => bigBang2BallsHeadOn(new Universe(600, 300))
        case "1" => bigBangTwoWallsAndABall(new Universe(600, 300))
        case "0" => bigBangTwoBalls45Angle(new Universe(600, 300))
        case _ => bigBangMovingAndStaticBall(new Universe(600, 300))
      }
      
      val observer = new Observer(universe)
      observer.observe()
    }
}
