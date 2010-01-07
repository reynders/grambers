package grambers

import java.lang.Math._

class Vector(val i : Double, val j : Double) {
  
  var name : String = ""  
    
  /*
   float 	angle(Vector2f v1)
          Returns the angle in radians between this vector and the vector parameter; the return value is constrained to the range [0,PI].
 float 	dot(Vector2f v1)
          Computes the dot product of the this vector and vector v1.
 float 	length()
          Returns the length of this vector.
 float 	lengthSquared()
          Returns the squared length of this vector.
 void 	normalize()
          Normalizes this vector in place.
 void 	normalize(Vector2f v1)
          Sets the value of this vector to the normalization of vector v1.
  */
  
  def dot(vector : Vector) : Double = {
    return (i*vector.i + j*vector.j)
  }
 
  def length : Double = {
    return (sqrt(i*i + j*j))
  }
  
  def angleBetween(vector : Vector) : Double = {
    return toDegrees(acos(this.normal.dot(vector.normal)))
  }
  
  def normal : Vector = {
    return new Vector(i/length, j/length)  
  }
  
  def eq(vector : Vector) : Boolean = {
    if (i == vector.i && j == vector.j)
      return true
    else
      return false
  }
  
  override def toString() : String = {
    return name + " (" + i + "i, " + j + "j)";
  }
}
