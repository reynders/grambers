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

      val leftSpeedTowardsCollision =  (leftThing.xSpeed * collUnitVector._1 + 
                                        leftThing.ySpeed * collUnitVector._2)

      val rightSpeedTowardsCollision = (rightThing.xSpeed * collUnitVector._1 + 
                                        rightThing.ySpeed * collUnitVector._2)                                               

      val leftSpeedPerpendicularWithCollision =  (-leftThing.xSpeed * collUnitVector._2 + 
                                                  leftThing.ySpeed * collUnitVector._1)

      val rightSpeedPerpendicularWithCollision = (-rightThing.xSpeed * collUnitVector._2 + 
                                                  rightThing.ySpeed * collUnitVector._1)                                                                                           
                                                  
      val leftNewSpeedTowardsCollision = leftSpeedTowardsCollision + (leftSpeedTowardsCollision- 
                                                                      rightSpeedTowardsCollision)

      val rightNewSpeedTowardsCollision = rightSpeedTowardsCollision + (rightSpeedTowardsCollision- 
                                                                        leftSpeedTowardsCollision)                                               
                                                                      
      val leftNewXspeed = (leftNewSpeedTowardsCollision * collUnitVector._1) - 
                           (leftSpeedPerpendicularWithCollision * collUnitVector._2)
      val leftNewYspeed = (leftNewSpeedTowardsCollision * collUnitVector._2) + 
                           (leftSpeedPerpendicularWithCollision * collUnitVector._1)

      val rightNewXspeed = (rightNewSpeedTowardsCollision * collUnitVector._1) - 
                            (rightSpeedPerpendicularWithCollision * collUnitVector._2)
      val rightNewYspeed = (rightNewSpeedTowardsCollision * collUnitVector._2) + 
                           (rightSpeedPerpendicularWithCollision * collUnitVector._1)      

      leftThing.setSpeedAndDirection(leftNewXspeed, leftNewYspeed)
      rightThing.setSpeedAndDirection(rightNewXspeed, rightNewYspeed)

/*
// where x1,y1 are center of ball1, and x2,y2 are center of ball2
double distance = Math.sqrt(dx*dx+dy*dy);

// Unit vector in the direction of the collision
double cx=dx/distance, cy=dy/distance;

// Projection of the velocities in these axes
double va1=(ax*cx+ay*cy), vb1=(-ax*cy+ay*cx);

double va2=(bx*cx+by*cy), vb2=(-bx*cy+by*cx);

// New velocities in these axes (after collision): ed<=1,  for elastic collision ed=1
double vaP1=va1 + (1+ed)*(va2-va1)/(1+mass1/mass2);
double vaP2=va2 + (1+ed)*(va1-va2)/(1+mass2/mass1);

// Undo the projections
ax=vaP1*cx-vb1*cy;  ay=vaP1*cy+vb1*cx;// new vx,vy for ball 1 after collision
bx=vaP2*cx-vb2*cy;  by=vaP2*cy+vb2*cx;// new vx,vy for ball 2 after collision                                          
      */
      calculateCollisionAngle(leftThing, rightThing)
    }

    def collideUsingVectors2 (leftThing : Thing, rightThing : Thing) {

      val lVector = new Vector(leftThing.xSpeed, leftThing.ySpeed)       
      val rVector = new Vector(rightThing.xSpeed, rightThing.ySpeed)
      val collisionUnitVector = new Vector((rightThing.location._1 - leftThing.location._1), (rightThing.location._2 - leftThing.location._2)).unitVector
      //val collisionUnitVector = collisionVector.unitVector
      
lVector.name = "l"
rVector.name = "r"
collisionUnitVector.name = "col"
      
      val massProportion = 2
      val diffOfVelocities = lVector - rVector
      val impulse = diffOfVelocities dot collisionUnitVector
      
      if (impulse > 0) {
        println("Impact already happened, no need to act")
        return
      }
        
diffOfVelocities.name = "dov"
println(lVector+ " - " + rVector + " - " + collisionUnitVector + " - " + diffOfVelocities + " - " + impulse)

      // float j = -(1.0f + cor) * impulse / (m1inverse + m2inverse);
      val relativeCollisionImpulse = (2*impulse) / massProportion
      var adjustVector = collisionUnitVector * relativeCollisionImpulse
            
      leftThing.setSpeedAndDirection(lVector - adjustVector)
      rightThing.setSpeedAndDirection(rVector + adjustVector)
      
      // Impulse is 2*(m1*m2/m1+m2)*((lvector-rvector) dot n) * n
      // From http://www.euclideanspace.com/physics/dynamics/collision/twod/index.htm#twod
      /*bool applyReponse(sphere& a, rectangle& b, const Vector& normal)
{
    // inverse masses (for static objects, inversemass = 0).
    float ima = a.m_inverseMass;
    float imb = b.m_inverseMass;
    float im  = ima + imb;
    if(im < 0.000001f) im = 1.0f;

    // impact velocity along normal of collision 'n'
    Vector v = (a.m_velocity - b.m_velocity);
    float vn = v.dotProduct(normal);

    // objects already separating, no reflection
    if (vn > 0.0f) return true;

    const float cor = 0.7f; // coefficient of restitution. Arbitrary value, in range [0, 1].
  
    // relative collision impulse
    float j = -(1.0f + cor) * vn / (im);
  
    // apply collision impulse to the two objects
    a.m_velocity += normal * (j * ima);
    b.m_velocity -= normal * (j * imb);

    return true;

    void collisionResponse(sphere& s, rectangle& r)
{
    Vector closest = closestPoint(s.m_centre, r);
    Vector normal = (s.m_centre - closest);
    normal.normalise();
    applyReponse(s, r, normal);
}

}
      */      
    }

    /*
    http://www.phy.ntnu.edu.tw/ntnujava/index.php?topic=4
    Vap2=((m1-m2)/(m1+m2))vap+ (2m2/(m1+m2))Vbp and
  Vbp2=(2 m1/(m1+m2))Vap+((m2-m1)/(m2+m1))Vbp
  The tangential component is not changed Van2=Van, and Vbn2=Vbn
4. Calculate the vector sum for velocity of each ball after collision
  Va2=Vap2+Van2 and Vb2=Vb2p+Vbn2 (vector summation)
    */
    def collideUsingVectors (leftThing : Thing, rightThing : Thing) {

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
   
      // Buggy
      val r2lImpulse = collisionUnitVector * (rVector dot collisionUnitVector)

      val l2rNormal = lVector - l2rImpulse
      val r2lNormal = rVector - r2lImpulse
      
      val l2rVelocityAfterCollision = l2rImpulse + (l2rImpulse + r2lImpulse)
      val r2lVelocityAfterCollision = r2lImpulse + (l2rImpulse + r2lImpulse)

l2rImpulse.name = "l2rI"
r2lImpulse.name = "r2lI"

      println(lVector+ " - " + rVector + " - " + l2rImpulse + " - " + r2lImpulse + " - " +
              (l2rNormal - l2rVelocityAfterCollision) + " - " + (r2lNormal - r2lVelocityAfterCollision))
      
      leftThing.setSpeedAndDirection(l2rNormal - l2rVelocityAfterCollision)
      rightThing.setSpeedAndDirection(r2lNormal - r2lVelocityAfterCollision)
    }
    
    
    def collideThings {
      for (startFrom <- 0 until things.size) {
        for (right <- startFrom until things.size) {
          if (things(startFrom) != things(right)) {
            if (things(startFrom).collidesWith(things(right)))
              //collide(things(startFrom), things(right))
              collideUsingVectors(things(startFrom), things(right))
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
