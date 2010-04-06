package grambers

object Config {
  var fps = 0
  var worldUpdates = 0
  var measurementStartTime = currentTimeMillis    
  var alphaFixOn : Boolean = true  

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
  
  def currentTimeMillis : Long = System.nanoTime / 1000000
}
