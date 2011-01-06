Backlog
-------
* Walls of any angle
* Fix bug on Mac OS X where rightmost column of pixels flicker white
* IOC (http://programming-scala.labs.oreilly.com/ch13.html#DependencyInjectionInScala)
* Polygon shapes
* Switch to use the scala 2.8 Swing implementation 
  (http://www.scala-lang.org/sites/default/files/sids/imaier/Mon,%202009-11-02,%2008:55/scala-swing-design.pdf)
* Bug: static things should have "infinite" mass when calculating collisiongs
* Introduce gravitation force
* Make it impossible to add a thing to universe that overlaps an existing one
* Shape to case class (http://programming-scala.labs.oreilly.com/ch06.html#CaseClasses)
* Scalafy code 

Done
-----
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


Material 
---------
http://www.metanetsoftware.com/technique/tutorialA.html#appendixA
http://www.euclideanspace.com/physics/dynamics/collision/twod/index.htm#twod
http://www.yov408.com/html/tutorials.php?s=74
http://www.plasmaphysics.org.uk/collision2d.htm
http://books.google.gr/books?id=lRUj-nhQRu8C&pg=PA836&lpg=PA836&dq=game%2Bprogramming%2Bvectors%2Bphysics&source=bl&ots=7vD94EI8-x&sig=X8DEWfrx7602Ijw83tXbtVbCvPk&hl=el&ei=6DwzSpyTHYKD_Ab9n42fCg&sa=X&oi=book_result&ct=result&resnum=2#v=onepage&q=game%2Bprogramming%2Bvectors%2Bphysics&f=false

circle-box
---
http://blog.generalrelativity.org/actionscript-30/collision-detection-circleline-segment-circlecapsule/
http://board.flashkit.com/board/showthread.php?t=773872
http://www.gamedev.net/community/forums/topic.asp?topic_id=520224
http://www.idevgames.com/forum/showthread.php?t=2868

Physics, time etc
http://gafferongames.com/game-physics/
http://www.gphysics.com/

Git
------------
git://github.com/reynders/grambers.git
Give this clone URL to anyone.
git clone git://github.com/reynders/grambers.git
Your Clone URL: 	
git@github.com:reynders/grambers.git 

Global setup:

  Download and install Git
  git config --global user.name "Your Name"
  git config --global user.email joonas.reynders@iki.fi
  Add your public key
        
Next steps:

  mkdir grambers
  cd grambers
  git init
  touch README
  git add README
  git commit -m 'first commit'
  git remote add origin git@github.com:reynders/grambers.git
  git push origin master
      
Existing Git Repo?

  cd existing_git_repo
  git remote add origin git@github.com:reynders/grambers.git
  git push origin master

