package grambers

import java.lang.Math._

class Vector(val i : Double, val j : Double) {
  
  var name : String = ""  
    
  def dot(vector : Vector) : Double = {
    return (i*vector.i + j*vector.j)
  }
 
  def length : Double = {
    return (sqrt(i*i + j*j))
  }

  def lengthSquared : Double = {
    return (i*i + j*j)
  }
  
  def angleBetween(vector : Vector) : Double = {
    return toDegrees(acos(this.unitVector.dot(vector.unitVector)))
  }
  
  def unitVector : Vector = {
    return new Vector(i/length, j/length)  
  }
  
  /**
    http://www.metanetsoftware.com/technique/tutorialA.html#appendixA
    The formula for projecting vector a onto vector b is:
  
    proj.x = ( dp / (b.x*b.x + b.y*b.y) ) * b.x;
    proj.y = ( dp / (b.x*b.x + b.y*b.y) ) * b.y;
  
    where dp is the dotprod of a and b: dp = (a.x*b.x + a.y*b.y)
  
    Note that the result is a vector; also, (b.x*b.x + b.y*b.y) is simply the length of b squared.
  
    If b is a unit vector, (b.x*b.x + b.y*b.y) = 1, and thus a projected onto b reduces to:
    proj.x = dp*b.x;
    proj.y = dp*b.y;
  */
  def projectionOn(vector : Vector) : Vector = {
    val dotProduct = this dot vector
    val xProj = (dotProduct / vector.lengthSquared) * vector.i
    val yProj = (dotProduct / vector.lengthSquared) * vector.j
    return new Vector(xProj, yProj)
  }
  
  override def equals(vector : Any) =  vector match {    
    case that : Vector => 
      this.i == that.i && this.j == that.j
    case _ => 
      false      
  }
  
  override def toString() : String = {
    return name + " (" + i + "i, " + j + "j)";
  }
}
