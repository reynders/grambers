package grambers

object Main {
    
    def bigBang() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1.0
      yellowCircle.direction = 90
      yellowCircle.location = (50, 50)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(0)}
      universe.things += yellowCircle

      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1.0
      redCircle.direction = 270
      redCircle.location = (120, 50)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(0)}
      universe.things += redCircle
      
      return universe
    }

    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
