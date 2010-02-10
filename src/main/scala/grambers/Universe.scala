package grambers

import scala.collection.mutable._
import java.lang.Math._
//import java.lang.System._

class Universe(val WIDTH : int, val HEIGHT : int) {
  val staticThings : ArrayBuffer[StaticThing] = new ArrayBuffer[StaticThing]
  val movingThings : ArrayBuffer[MovingThing] = new ArrayBuffer[MovingThing]
  
  var millisecondsSinceBigBang = 0; 
  
  def currentTimeMillis : Long = System.nanoTime / 1000000
  
  def addThing(thing : Thing) {
    thing match {
      case movingThing : MovingThing => movingThings += movingThing
      case staticThing : StaticThing => staticThings += staticThing
      case _ => println("addThing does not know what to do with " + thing)
    }
  }
  
  def calculateCollisionAngle(leftThing : Thing, rightThing : Thing) : Double = {
    val xDiff : Double = (leftThing.center.x - rightThing.center.x)
    val yDiff : Double = (leftThing.center.y - rightThing.center.y)
    
    val collisionAngle = toDegrees(atan(xDiff/ yDiff))

    return collisionAngle
  }
      
  def resolveCollision(leftThing : Thing, rightThing : Thing): Unit = {
println("Resolving " + leftThing + " collision with " + rightThing)
    val lVector = new Vector(leftThing.xSpeed, leftThing.ySpeed)       
    val rVector = new Vector(rightThing.xSpeed, rightThing.ySpeed)      
    val collisionUnitVector = Shape.collisionUnitVector(leftThing.shape, rightThing.shape)
//println("Collision vector: " + collisionUnitVector)
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

//println(leftThing + ":" + lVector + " - " + l2rImpulse + " -- " + l2rNormal + " --- " + l2rVelocityAfterCollision)
//println(rightThing + ":" + rVector + " - " + r2lImpulse + " -- " + r2lNormal + " --- " + r2lVelocityAfterCollision) 

    leftThing.setSpeedAndDirection(l2rNormal + l2rVelocityAfterCollision)
    rightThing.setSpeedAndDirection(r2lNormal + r2lVelocityAfterCollision)
  }
  

  def collide(movingThings : Seq[MovingThing], staticThings : Seq[StaticThing]) {
    movingThings.foreach(leftMovingThing => {
      movingThings.foreach(rightMovingThing => {
        if (leftMovingThing != rightMovingThing && leftMovingThing.collidesWith(rightMovingThing))
          resolveCollision(leftMovingThing, rightMovingThing)
      })
      
      staticThings.foreach(staticThing => {
        if (staticThing.collidesWith(leftMovingThing))
          resolveCollision(staticThing, leftMovingThing)
      })
    })
  }

  def moveOneMillisecondWorth(movingThings : Seq[MovingThing]) {
    movingThings.foreach(movingThing => {
      movingThing.doYourThing(movingThing)
      var newX = (movingThing.center.x + movingThing.xSpeed/1000)%WIDTH
      if (newX < 0 ) newX += WIDTH 
      var newY = (movingThing.center.y + movingThing.ySpeed/1000)%HEIGHT
      if (newY < 0 ) newY += HEIGHT
    
      movingThing.center = Point(newX, newY)
    })
  }
  
  def advanceTime(msToAdvance : int) {      
    for(i <- 1 to msToAdvance) {
      moveOneMillisecondWorth(movingThings)
      collide(movingThings, staticThings)
      millisecondsSinceBigBang += 1
    }
  }

  def msSinceLastWorldUpdate : Long = currentTimeMillis-lastWorldUpdateTime
  
  var worldUpdates = 0
  val worldUpdatesPerSecond = 50    
  var lastWorldUpdateTime = 0l
  
  def run(observer : Observer) {      
    val millisecondsBetweenWorldUpdates = 1000 / worldUpdatesPerSecond
    var nextWorldUpdateTime = currentTimeMillis
                 
    while (true) {
      val now = currentTimeMillis
         
      while (now > nextWorldUpdateTime) {             
        // We do not use REAL elapsed ms here because that would again be 
        // computer speed dependant
        advanceTime(millisecondsBetweenWorldUpdates)         
        nextWorldUpdateTime += millisecondsBetweenWorldUpdates        
        lastWorldUpdateTime = now
        worldUpdates+=1             
      }

      observer.observe()
    }
  }    
}
