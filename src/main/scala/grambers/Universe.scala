package grambers

class Universe() {
    val things : Array[Thing] = new Array[Thing](0)
    
    def bigBang {
        for(thing <- things) {
            println(thing)
        }
    }
}
