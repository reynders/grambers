package grambers

object Main {
    
    def bigBang() : Universe = {
      val universe = new Universe();
      val plainCircle = new grambers.Circle(10);
      universe.things += plainCircle
      
      return universe
    }
    

    def main(args:Array[String]) {
        val observer = new Observer(bigBang(), 500, 200)
        observer.observe()
    }
}
