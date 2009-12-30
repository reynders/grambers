package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var sinceBigBangInTicks = 0; 
    
    def collideThings {
      for (startFrom <- 0 until things.size) {
        for (right <- startFrom until things.size) {
          if (things(startFrom) != things(right)) {
            collide(things(startFrom), things(right))
            println("Colliding " + things(startFrom) + " with " + things(right))       
          }
        }
      }
    }
    
    def collide(left: Thing, right: Thing) {
    }
    
    def moveThings {
        for(thing <- things) {
            thing.doYourThing(thing)
            val xSpeed = thing.speed * sin(toRadians(thing.direction))
            val ySpeed = thing.speed * cos(toRadians(thing.direction)) * - 1
            thing.location = (thing.location._1 + xSpeed, thing.location._2 + ySpeed)
        }
    }
    
    def advanceTime(msToAdvance : int) {
      for(i <- 1 to msToAdvance) {
        moveThings
        sinceBigBangInTicks += 1
      }
    }
}
