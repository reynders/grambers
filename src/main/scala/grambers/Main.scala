package grambers

object Main {
    
    def bigBang() : Universe = {
      val universe = new Universe(500, 200);
      
      val yellowCircle = new grambers.Circle(10);
      yellowCircle.color = java.awt.Color.yellow
      yellowCircle.speed = 1
      yellowCircle.direction = 135
      yellowCircle.location = (50, 50)
      yellowCircle.doYourThing = (yellowCircle) => {yellowCircle.turn(3)}
      universe.things += yellowCircle

      val redCircle = new grambers.Circle(10);
      redCircle.color = java.awt.Color.red
      redCircle.speed = 1
      redCircle.direction = 180
      redCircle.location = (80, 50)
      redCircle.doYourThing = (redCircle) => {redCircle.turn(4)}
      universe.things += redCircle
      
      return universe
    }

    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
