// Programa para caluclar integraciones numericas usando el metodo Simpson 1/3

// Definimos una varaible que contendra nuestra funcion matematica

val fx = (x: Double) => - Math.pow(x, 2) + 8 * x - 12 // La variable: "fx" albergara nuestra funcion matematica

// Creamos una funcion denominada integracion la cual recibe 3 parametros:
// El primero una funcion lambda que recibe un valor de tipo Double y devuelve un valor de tipo Double
// El segundo y tercer parametro que recibe la funcion son valores de tipo Int

def integracion(num:Double => Double, a: Int, b: Int): Double = {

  val x1 = (a + b) / 2
  ((b - a) * (num(a) + 4 * num(x1) + num(b)) / 6)

}// En esta funcion se emplea el metodo Simpson para calcular la integral

// Se crea una funcion para calcular el margen de error que se presenta en cada aproximacion

def maergenErr(x: Double, y: Double) = Math.abs(x - y) // En esta funcion se emplea la funcion Math.abs la cual nos
                                                       // pemrite calcular el valor absoluto y asi obetener el error
                                                       // correspondiente

// Se crea una varaible la cual equivale a la funcion denominada integracion, pasandole asi los valores correspondiente  
val aproximacion = integracion(fx,3,5)

val err = maergenErr(7.33, aproximacion)


// A continuacion se crearan varias expresiones matematicas para calcular sus aproximaciones

// Ejercicio 2

val fx1 = (x : Double) => 3 * math.pow(x, 2)
val aproximacion1 = integracion(fx1,0,2)
val err1 = maergenErr(8, aproximacion)

// Ejercicio 3

val fx2 = (x: Double) =>  x + 2 * math.pow(x, 2) - math.pow(x, 3) + 5 * math.pow(x, 4)
val aproximacion2 = integracion(fx,-1,1)
val err2 = maergenErr(3.333, aproximacion)

// Ejercicio 4

val fx3 = (x: Double) =>  (2*x + 1) / math.pow(x,2) + x
val aproximacion3 = integracion(fx,1,2)
val err3 = maergenErr(1.09861, aproximacion)

// Ejercicio 5

val fx4 = (x: Double) => math.pow(math.E, x)
val aproximacion4 = integracion(fx,0,1)
val err4 = maergenErr(1.71828, aproximacion)

// Ejercicio 6

val fx5 = (x: Double) => 1 / math.sqrt(x-1)
val aproximacion5 = integracion(fx,2,3)
val err5 = maergenErr(0.828427, aproximacion)

// Ejercicio 7

val fx6 = (x: Double) => 1 / 1 + math.pow(x,2)
val aproximacion6 = integracion(fx,0,1)
val err6 = maergenErr(0.785398, aproximacion)