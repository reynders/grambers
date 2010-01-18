package grambers

import java.lang.Math._

class Vector(val i : Double, val j : Double) {
  
  var name : String = ""  
  lazy val length = sqrt(i*i + j*j)
  lazy val lengthSquared = (i*i + j*j)   
  lazy val unitVector = new Vector(i/length, j/length)
  
  def dot(vector : Vector) : Double = {
    return (i*vector.i + j*vector.j)
  }
   
  def angleBetween(vector : Vector) : Double = {
    return toDegrees(acos(this.unitVector.dot(vector.unitVector)))
  }

  def projectionOn(vector : Vector) : Vector = {
    val dotProduct = this dot vector
    val xProj = (dotProduct / vector.lengthSquared) * vector.i
    val yProj = (dotProduct / vector.lengthSquared) * vector.j
    return new Vector(xProj, yProj)
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
