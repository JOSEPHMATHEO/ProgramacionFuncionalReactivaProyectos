val f = (x: Double) => 3.0 * x

def f1(x: Double): Double = Math.pow(x, 3)

def f1x(num: Double => Double, a: Int ): Double = {
  val h = 0.333
  (num(a + h) - num(a - h) ) / (2 * h)

}

val evaluar = f1x(f1,3)
val evaluar2 = f1x(f,3)