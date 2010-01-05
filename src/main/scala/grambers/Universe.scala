package grambers

import scala.collection.mutable._
import java.lang.Math._

class Universe(val WIDTH : int, val HEIGHT : int) {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    var sinceBigBangInTicks = 0; 
    
    def calculateCollisionAngle(leftThing : Thing, rightThing : Thing) : Double = {
      val xDiff : Double = (leftThing.location._1 - rightThing.location._1)
      val yDiff : Double = (leftThing.location._2 - rightThing.location._2)
      
      val collisionAngle = toDegrees(atan(xDiff/ yDiff))
  
      return collisionAngle
    }
        
    def collide (leftThing : Thing, rightThing : Thing) {

      val distance : Double = leftThing.distanceFrom(rightThing)
      val collUnitVector : (Double, Double) = (((leftThing.location._1 - rightThing.location._1)/distance),  
                                               ((leftThing.location._2 - rightThing.location._2)/distance))
                                          
      //val left_delta_x = left.location                                                 
      //val leftVelocityTowardsCollision = ()
/*
// where x1,y1 are center of ball1, and x2,y2 are center of ball2

double distance = Math.sqrt(dx*dx+dy*dy);

// Unit vector in the direction of the collision
double cx=dx/distance, cy=dy/distance;

// Projection of the velocities in these cxes
double va1=(ax*cx+ay*cy), vb1=(-ax*cy+ay*cx);

double va2=(bx*cx+by*cy), vb2=(-bx*cy+by*cx);

// New velocities in these cxes (after collision): ed<=1,  for elastic collision ed=1
double vaP1=va1 + (1+ed)*(va2-va1)/(1+mass1/mass2);
double vaP2=va2 + (1+ed)*(va1-va2)/(1+mass2/mass1);

// Undo the projections
ax=vaP1*cx-vb1*cy;  ay=vaP1*cy+vb1*cx;// new vx,vy for ball 1 after collision
bx=vaP2*cx-vb2*cy;  by=vaP2*cy+vb2*cx;// new vx,vy for ball 2 after collision                                          
      */
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
            thing.location = (thing.location._1 + thing.xSpeed, thing.location._2 + thing.ySpeed)
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
