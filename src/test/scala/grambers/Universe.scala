package grambers

import java.util._

class Universe() {
    val things : List[Thing] = new ArrayList[Thing]
    
    def bigBang {
        things.foreach(thing => {
            println(thing);
        }
    }
}
