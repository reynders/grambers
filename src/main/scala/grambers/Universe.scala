package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var millisecondsSinceBigBang = 0; 
    
    def calculateCollisionAngle(leftThing : Thing, rightThing : Thing) : Double = {
      val xDiff : Double = (leftThing.location._1 - rightThing.location._1)
      val yDiff : Double = (leftThing.location._2 - rightThing.location._2)
      
      val collisionAngle = toDegrees(atan(xDiff/ yDiff))
  
      return collisionAngle
    }
        
    def resolveCollision(leftThing : Thing, rightThing : Thing): Unit = {
println("Resolving " + leftThing + " collision with " + rightThing)
      val lVector = new Vector(leftThing.xSpeed, leftThing.ySpeed)       
      val rVector = new Vector(rightThing.xSpeed, rightThing.ySpeed)      
      val collisionUnitVector = Shape.collisionUnitVector(leftThing.shape, rightThing.shape)
println("Collision vector: " + collisionUnitVector)
      if (((lVector dot collisionUnitVector) - (rVector dot collisionUnitVector)) < 0) {
        println("Impact already happened, no need to act")
        return
      }

      val l2rImpulse = collisionUnitVector * (lVector dot collisionUnitVector) 
      val r2lImpulse = collisionUnitVector * (rVector dot collisionUnitVector)

      val l2rNormal = lVector - l2rImpulse
      val r2lNormal = rVector - r2lImpulse
      
      val l2rVelocityAfterCollision = l2rImpulse*((leftThing.mass-rightThing.mass)/(leftThing.mass+rightThing.mass)) + 
                                      r2lImpulse*((2*rightThing.mass)/(leftThing.mass + rightThing.mass))
                                      
      val r2lVelocityAfterCollision = l2rImpulse*((2*leftThing.mass)/(leftThing.mass+rightThing.mass)) + 
                                      r2lImpulse*((rightThing.mass-leftThing.mass)/(rightThing.mass + leftThing.mass))

println(leftThing + ":" + lVector + " - " + l2rImpulse + " -- " + l2rNormal + " --- " + l2rVelocityAfterCollision)
println(rightThing + ":" + rVector + " - " + r2lImpulse + " -- " + r2lNormal + " --- " + r2lVelocityAfterCollision) 

      leftThing.setSpeedAndDirection(l2rNormal + l2rVelocityAfterCollision)
      rightThing.setSpeedAndDirection(r2lNormal + r2lVelocityAfterCollision)
    }
    
      
    def collide(things : Seq[Thing]) {
      for (startFrom <- 0 until things.size) {
        for (right <- startFrom until things.size) {
          if (things(startFrom) != things(right) &&
              !(things(startFrom).isInstanceOf[StaticThing] && things(right).isInstanceOf[StaticThing])) {
            if (things(startFrom).collidesWith(things(right)))  {
              (things(startFrom), things(right)) match {
                case (left : RoundThing, right : RoundThing) => resolveCollision(left, right)
                case (left : RoundThing, right : Box) => resolveCollision(right, left)
                case (left : Box, right : RoundThing) => resolveCollision(left, right)
                case (left : Thing, right : Thing) => println("Don't know how to handle collision between " + left + " and " + right)
              }
            }
          }
        }
      }
    }

    def moveOneMillisecondWorth(things : Seq[Thing]) {
        for(thing <- things) {
            thing.doYourThing(thing)
            var newX = (thing.location._1 + thing.xSpeed/1000)%WIDTH
            if (newX < 0 ) newX += WIDTH 
            var newY = (thing.location._2 + thing.ySpeed/1000)%HEIGHT
            if (newY < 0 ) newY += HEIGHT
          
            thing.location = (newX, newY)
        }
        
    }
    
    def advanceTime(msToAdvance : int) {      
      for(i <- 1 to msToAdvance) {
        moveOneMillisecondWorth(things)
        collide(things)
        millisecondsSinceBigBang += 1
      }
    }
}
