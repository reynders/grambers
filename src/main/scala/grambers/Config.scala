package grambers

object Config {
  // World settings
  val worldUpdatesPerSecond = 60
  val worldUpdateDt : Float = 1.0f/worldUpdatesPerSecond
  val velocityIterations = 10
  val positionIterations = 10

  // Gfx settings
  val imageType = java.awt.image.BufferedImage.TYPE_INT_ARGB
  val ROTATED_IMAGE_COUNT = 36
  val limitFps = false
  val fpsLimit = 5
  val bgColor= java.awt.Color.LIGHT_GRAY

  // Measurements
  var fps = 0
  var worldUpdates = 0
  var measurementStartTime = currentTimeMillis

  // Debug
  var debugOn : Boolean = false
  val debugDrawShapes : Boolean = false
  val debugDrawShapesColor : java.awt.Color = java.awt.Color.RED

  def measurementSamplePeriodMs : Long = {
    currentTimeMillis - measurementStartTime
  }

  def resetAnimationMeasurements {
    fps = 0; worldUpdates = 0; measurementStartTime = currentTimeMillis ;
  }

  def showAnimationStatistics {
    if (measurementSamplePeriodMs > 0)
      println((fps/(measurementSamplePeriodMs/1000)) + " FPS, " +
              (worldUpdates/(measurementSamplePeriodMs/1000)) + " world updates")
  }

  def currentTimeMillis : Long = // System.currentTimeMillis
                                 System.nanoTime / 1000000
}
