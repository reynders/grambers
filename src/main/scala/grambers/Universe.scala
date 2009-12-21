package grambers

import scala.collection.mutable._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var ticksInMs = 0; 
    
    def bigBang {
        for(thing <- things) {
            println(thing)
        }
    }
    
    def advanceTime(msToAdvance : int) {
              
    }
}
