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
  
  // http://www.metanetsoftware.com/technique/tutorialA.html#appendixA
  def projectionOn(vector : Vector) : Vector = {
    val dotProduct = this dot vector
    val xProj = (dotProduct / vector.lengthSquared) * vector.i
    val yProj = (dotProduct / vector.lengthSquared) * vector.j
    return new Vector(xProj, yProj)
  }
  
  def rightHandNormal : Vector = { 
    return new Vector(-j, i)
  }

  def -(vector : Vector) : Vector = {
    return new Vector(this.i - vector.i, this.j - vector.j)
  }

  def +(vector : Vector) : Vector = {
    return new Vector(this.i + vector.i, this.j + vector.j)
  }

  def *(scalar : Double) : Vector = {
    return new Vector(this.i * scalar, this.j * scalar)
  }
  
  def leftHandNormal : Vector = { 
    return new Vector(j, -i)
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
