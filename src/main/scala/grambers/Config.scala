package grambers

object Config {
  val imageType = java.awt.image.BufferedImage.TYPE_INT_ARGB 
  
  val worldUpdatesPerSecond = 60
  val worldUpdateDt : Float = 1.0f/worldUpdatesPerSecond
  val velocityIterations = 10
  val positionIterations = 10
  var fps = 0
  var worldUpdates = 0
  var measurementStartTime = currentTimeMillis    
  var debugOn : Boolean = false

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
