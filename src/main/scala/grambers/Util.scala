package grambers

import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.awt.{Transparency, GraphicsConfiguration, GraphicsDevice, GraphicsEnvironment}

object Util {

  val log = LoggerFactory.getLogger("grambers")

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

  /**
   * @param w Desired width
   * @param h Desired height
   * @param transparency java.awt.Transparency value
   * @return a performance optimized image for this device
   */
  def createBufferedImage(w : Int, h : Int, transparency : Int) : BufferedImage = {
    val env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    val device = env.getDefaultScreenDevice();
    val config : GraphicsConfiguration = device.getDefaultConfiguration();
    return config.createCompatibleImage(w, h, transparency);
  }
}