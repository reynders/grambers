Backlog
-------
* Take first game map into use
* Other game objects: start point, end point
* Mass properties to gameobject xml
* Force information to gameobject xml
* Check performance bottlenecks
* Find out why there are pauses (-XInt helps?)
* Int Point
* IOC (http://programming-scala.labs.oreilly.com/ch13.html#DependencyInjectionInScala)
* Switch to use the scala Swing implementation
  (http://www.scala-lang.org/sites/default/files/sids/imaier/Mon,%202009-11-02,%2008:55/scala-swing-design.pdf)
* Scalafy code
* sbt build

Done
-----
* Move object manipulation into game loop instead of key event handler
* Fix keyboard input: key down -> apply, key up -> stop applying
* Integrated with jbox2d
* Polygon shapes
* Introduce gravitation force
* Shape to case class (http://programming-scala.labs.oreilly.com/ch06.html#CaseClasses)
* Fix bug on mac: tile buffering is one tile row too late (unreproducible)
* Rewrite game loop to update objects for only specific point of time,
  then interpolate until next update
* Try out git branching locally
* Detach view window coordinates from universe coordinates
* Support for sprites
* Better demo that also supports performance profiling -Xprof
* Support for smooth animation between world state updates
* Refactor the demo class (Main)
* Shape Point
* Detach universe time from drawing speed
* Static objects

