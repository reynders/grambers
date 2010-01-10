package grambers

object Main {
    
    def bigBang1() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 2.2
      yellowCircle.direction = 315.0
      yellowCircle.location = (100, 100)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      
      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1.0
      redCircle.direction = 135
      redCircle.location = (150, 50)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(0)}
      
      universe.things += redCircle
      universe.things += yellowCircle
      
      return universe
    }

    def bigBang2() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1.0
      yellowCircle.direction = 0.0
      yellowCircle.location = (50, 100)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      
      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1.0
      redCircle.direction = 180.0
      redCircle.location = (100, 100)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(0)}
      
      universe.things += redCircle
      universe.things += yellowCircle
      
      return universe
    }

    def bigBang() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1.0
      yellowCircle.direction = 45.0
      yellowCircle.location = (50, 50)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      
      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1.0
      redCircle.direction = 135
      redCircle.location = (100, 50)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(0)}
      
      universe.things += redCircle
      universe.things += yellowCircle
      
      return universe
    }

    def bigBang_static() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1.0
      yellowCircle.direction = 0.0
      yellowCircle.location = (50, 50)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      
      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 2.0
      redCircle.direction = 180
      redCircle.location = (100, 50)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(0)}
      
      universe.things += redCircle
      universe.things += yellowCircle
      
      return universe
    }    
    
    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
