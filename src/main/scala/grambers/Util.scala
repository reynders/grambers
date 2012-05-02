package grambers

object Util {
  def pointArrayStrToPointArray(str : String) : Array[Point] = {
    if (str.trim.equals(""))
      new Array[Point](0)
    else
      str.split(" ").map { pair => strPointToPoint(pair)}
  }

  def strPointToPoint(str : String) : Point = {
  	val numbers = str.split(",")
  	return Point(numbers(0), numbers(1))
  }

  def parseInt(str : String, default : Int) : Int = {
    if (str.equals(""))
      default
    else
      str.toInt
  }

  def parseDouble(str : String, default: Double) : Double = {
    if (str.equals(""))
      default
    else
      str.toDouble
  }
}