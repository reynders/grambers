package grambers

// Playfield class, used to demo the gaming library
object Main {
    
    def bigBang1() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 2.2
      yellowRoundThing.direction = 315.0
      yellowRoundThing.location = (100, 100)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redRoundThing = new grambers.RoundThing(10);
      redRoundThing.color = java.awt.Color.red
      redRoundThing.speed = 1.0
      redRoundThing.direction = 135
      redRoundThing.location = (150, 50)
      redRoundThing.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redRoundThing
      universe.things += yellowRoundThing
      
      return universe
    }

    def bigBang2() : Universe = {
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

    def bigBang5() : Universe = {
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

    def bigBang_static() : Universe = {
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
    
    def bigBang() : Universe = {
      val universe = new Universe(600, 300);
      
      val yellowRoundThing = new grambers.RoundThing(10);
      yellowRoundThing.color = java.awt.Color.yellow
      yellowRoundThing.speed = 1.0
      yellowRoundThing.direction = 0.0
      yellowRoundThing.location = (50, 50)
      yellowRoundThing.doYourThing = (yellowRoundThing) => {yellowRoundThing.turn(0)}
      
      val redBox = new grambers.Box(20, 50);
      redBox.color = java.awt.Color.red
      redBox.speed = 1.0
      redBox.direction = 180
      redBox.location = (100, 50)
      redBox.doYourThing = (redRoundThing) => {redRoundThing.turn(0)}
      
      universe.things += redBox
      universe.things += yellowRoundThing
      
      return universe
    }  
    
    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
