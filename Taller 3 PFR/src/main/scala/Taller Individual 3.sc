//val transform = (radianes: Double) => radianes * 180 / Math.PI
//val grados = transform(32)
//def calcTaxes(monto: Double, iva: Int): Double = monto * iva / 100
//val calcIVA = calcTaxes(_, 12)
//val IVA2Pay = calcIVA(100)
//def transformGrades(unity: Char) =
//   unity match
//      case 'C' => (gCenti: Double) => (gCenti * 9 / 5.0) + 32
//      case 'F' => (gFahre: Double) => (gFahre - 32) * 5 / 9.0
//      case _ => (grade: Double) => grade
//
//val fToC = transformGrades('F')
//val gradosCenti = fToC(125)
//val cToF = transformGrades('C')
//val gradosFahre = cToF(37.5)
//val error = transformGrades('N')
//val grados = error(321)
//val evaluate = (f: String => Boolean, text: String) => f(text)
//val longitudMayor4 = (text: String) => text.length > 4
//val esMayor4 = evaluate(longitudMayor4, "Universidad")

def isPrime(n: Int): Boolean = (2 to n - 1).forall(n % _ != 0)

def evaluar (num: Int => Boolean, n: Int ): String = if (num(n)) then (n +"Es primo") else (n +"No es primo")
val eval = evaluar(isPrime,3)
