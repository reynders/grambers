package grambers

import scala.collection.mutable._

class Universe() {
    val things : ArrayBuffer[Thing] = new ArrayBuffer[Thing]
    
    def bigBang {
        for(thing <- things) {
            println(thing)
        }
    }
}
