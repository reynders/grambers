package grambers

object Main {
    
    def bigBang() : Universe = {
      val universe = new Universe(500, 200);
      val plainCircle = new grambers.Circle(10);
      plainCircle.speed = 1
      plainCircle.direction = 135
      plainCircle.location = (50, 50)
      plainCircle.doYourThing = (plainCircle) => {plainCircle.turn(3)}
      universe.things += plainCircle
      
      return universe
    }

    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
