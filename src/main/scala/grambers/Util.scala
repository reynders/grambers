package grambers

object Util {
  def pointArrayStrToPointArray(str : String) : Array[Point] = {
    if (str.trim.equals(""))
      new Array[Point](0)
    else
      str.split(" ").map { pair => Point(pair.split(",")(0), pair.split(",")(1))}
  }
}