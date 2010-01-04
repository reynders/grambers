package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var sinceBigBangInTicks = 0; 
    
    def calculateCollisionAngle(leftThing : Thing, rightThing : Thing) : Double = {
      val xDiff : Double = (leftThing.location._1 - rightThing.location._1)
      val yDiff : Double = (leftThing.location._2 - rightThing.location._2)
      
      val collisionAngle = toDegrees(atan(xDiff/ xDiff))
  
      return collisionAngle
    }
    
    def collide (leftThing : Thing, rightThing : Thing) {
      leftThing.speed = 0;
      rightThing.speed = 0;
      calculateCollisionAngle(leftThing, rightThing)
    }
    
    def collideThings {
      for (startFrom <- 0 until things.size) {
        for (right <- startFrom until things.size) {
          if (things(startFrom) != things(right)) {
            if (things(startFrom).collidesWith(things(right)))
              collide(things(startFrom), things(right))       
          }
        }
      }
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
        collideThings
        sinceBigBangInTicks += 1
      }
    }
}
