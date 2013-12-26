A thrust / Turbo Raketti / Gravity Force clone game and a game platform.

![Screencapture of a demo map](https://github.com/reynders/grambers/raw/master/resources/screencap/grambers_1.png)

Supports Tiled maps (htt://www.mapeditor.org) and uses jbox2d for physics.

Build instructions
------------------
Download and install Maven 3+
Build with "mvn test"

Running
-------
./run.sh grambers.Demo G

for more demos see Demo.scala

Backlog
-------
* Support for arbitrary size game window
* Check performance bottlenecks
* Force debug draw
* Other game objects: start point, end point
* Find out why there are pauses (-XInt helps?)
* Switch to use the scala Swing implementation
  (http://www.scala-lang.org/sites/default/files/sids/imaier/Mon,%202009-11-02,%2008:55/scala-swing-design.pdf)

Done
-----
* Int Point
* Use centralized platform specific bufferimage creation
* logging fw in use
* Force information to gameobject xml
* Take first game map into use
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

