package grambers

abstract class Thing {
    var location: (int, int) = (0, 0)
    var speed : int = 0
    var direction : int = 0
    
    def turn(degrees:int) {
        direction += degrees
    }
    
    def accelerate(amount : int) {
        speed += amount
    }
    
    override def toString : String = {
        location + ":" + speed + ":" + direction
    }
}

class Circle extends Thing {
}

class Box extends Thing {
}