package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var ticksSinceBigBangIn = 0; 
    
    def calculateCollisionAngle(leftThing : Thing, rightThing : Thing) : Double = {
      val xDiff : Double = (leftThing.location._1 - rightThing.location._1)
      val yDiff : Double = (leftThing.location._2 - rightThing.location._2)
      
      val collisionAngle = toDegrees(atan(xDiff/ yDiff))
  
      return collisionAngle
    }
        
    /*
    http://www.phy.ntnu.edu.tw/ntnujava/index.php?topic=4
      Vap2=((m1-m2)/(m1+m2))vap+ (2m2/(m1+m2))Vbp and
      Vbp2=(2 m1/(m1+m2))Vap+((m2-m1)/(m2+m1))Vbp
      The tangential component is not changed Van2=Van, and Vbn2=Vbn
      4. Calculate the vector sum for velocity of each ball after collision
      Va2=Vap2+Van2 and Vb2=Vb2p+Vbn2 (vector summation)
    */
    def resolveCollision(leftThing : Thing, rightThing : Thing) {

      val lVector = new Vector(leftThing.xSpeed, leftThing.ySpeed)       
      val rVector = new Vector(rightThing.xSpeed, rightThing.ySpeed)
      val collisionUnitVector = new Vector((rightThing.location._1 - leftThing.location._1), (rightThing.location._2 - leftThing.location._2)).unitVector
      
lVector.name = "l"
rVector.name = "r"
collisionUnitVector.name = "l2rCol"

      if (((lVector dot collisionUnitVector) - (rVector dot collisionUnitVector)) < 0) {
        println("Impact already happened, no need to act")
        return
      }

      val l2rImpulse = collisionUnitVector * (lVector dot collisionUnitVector) 
      val r2lImpulse = collisionUnitVector * (rVector dot collisionUnitVector)

      val l2rNormal = lVector - l2rImpulse
      val r2lNormal = rVector - r2lImpulse
      
      val l2rVelocityAfterCollision = l2rImpulse + (r2lImpulse - l2rImpulse)
      val r2lVelocityAfterCollision = r2lImpulse + (l2rImpulse - r2lImpulse)
      
l2rImpulse.name = "l2rI"
r2lImpulse.name = "r2lI"
l2rNormal.name = "l2rN"
r2lNormal.name = "r2lN"
l2rVelocityAfterCollision.name = "l2rVaC"
r2lVelocityAfterCollision.name = "r2lVaC"

      println(lVector+ " - " + rVector + " - " + l2rImpulse + " - " + r2lImpulse + " - " +
              l2rNormal + " - " + r2lNormal + " - " + l2rVelocityAfterCollision + " - " +r2lVelocityAfterCollision)
      
      leftThing.setSpeedAndDirection(l2rNormal + l2rVelocityAfterCollision)
      rightThing.setSpeedAndDirection(r2lNormal + r2lVelocityAfterCollision)
    }
    
    
    def collide(things : Seq[Thing]) {
      for (startFrom <- 0 until things.size) {
        for (right <- startFrom until things.size) {
          if (things(startFrom) != things(right)) {
            if (things(startFrom).collidesWith(things(right)))  {            
              resolveCollision(things(startFrom), things(right))          
            }
          }
        }
      }
    }

    def move(things : Seq[Thing]) {
        for(thing <- things) {
            thing.doYourThing(thing)
            thing.location = (thing.location._1 + thing.xSpeed, thing.location._2 + thing.ySpeed)
        }
    }
    
    def advanceTime(msToAdvance : int) {
      for(i <- 1 to msToAdvance) {
        move(things)
        collide(things)
        ticksSinceBigBangIn += 1
      }
    }
}
