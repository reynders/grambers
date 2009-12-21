package grambers

object Main {
    
    def bigBang() : Universe = {
      val universe = new Universe(500, 200);
      val plainCircle = new grambers.Circle(10, java.awt.Color.yellow);
      universe.things += plainCircle
      
      return universe
    }
    

    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
