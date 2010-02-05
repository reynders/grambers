class Test {
  def print(t : Test) : Boolean = {
    return true
  }
}

class Test2 extends Test {
}

val myTest = new Test2
println(myTest.print(myTest))
