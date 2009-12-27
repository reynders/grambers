package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var ticksInMs = 0; 
    
    def moveThings {
        for(thing <- things) {
            println("Moving" + thing)
            val xSpeed = thing.speed * sin(toRadians(thing.direction))
            val ySpeed = thing.speed * cos(toRadians(thing.direction)) * - 1
            thing.location = (thing.location._1 + xSpeed, thing.location._2 + ySpeed)
            
            thing.turn(1)
        }
    }
    
    def advanceTime(msToAdvance : int) {
      for(i <- 1 to msToAdvance) {
        moveThings
      }
    }
}
