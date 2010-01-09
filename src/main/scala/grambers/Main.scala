package grambers

object Main {
    
    def bigBang2() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1.0
      yellowCircle.direction = 135.0
      yellowCircle.location = (0, 50)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      
      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1.0
      redCircle.direction = 315
      redCircle.location = (50, 00)
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

    
    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
