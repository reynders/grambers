package grambers

import scala.collection.mutable._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var ticksInMs = 0; 
    
    def moveThings {
        for(thing <- things) {
            println("Moving" + thing)
            thing.location = (thing.location._1 + thing.speed, thing.location._2)
        }
    }
    
    def advanceTime(msToAdvance : int) {
      for(i <- 1 to msToAdvance) {
        moveThings
      }
    }
}
