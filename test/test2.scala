abstract class A {
  val a : Int
}

abstract class B extends A {
  val a = 1
  val b : Int
}

class C extends B {
  val b = 2
  var c = 0
  
  {
    c = 4
  }
}

println("Result: " + new C().b + " c = " + new C().c)
