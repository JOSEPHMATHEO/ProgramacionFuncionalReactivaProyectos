// def genero(genero: String)= (nombre:String) => if genero.equalsIgnoreCase("F") then "Doña " + nombre else
//          "Don " + nombre
//
//val saludXGenero = genero2("M")
//
//val squareSum = (x: Int, y: Int )=> (x*x + y*y)

def add1(x: Double) = x + 1

val sum1 = add1(_)

def addPlus1(x: Int, y: Int ) = x*y +1

val nulmSum1 = addPlus1(_,_)

add1(2)

nulmSum1(2,22)

// Funciones que reciben Funciones como parametro

def execute(f: (Int, Int) => Int, a: Int, b: Int) = f(a,b) * f(a,b)

val sumar = (a: Int, b: Int) => a + b

execute(sumar,4,5)

// ANother exercise

def createAdder(x: Int): Int => Int = (y: Int) => x + y

val add5 = createAdder(5)
add5(10)

def createAdder2(x: Int): Int => Int = y => x + y

val add5 = createAdder(5)



// Apuntes 2

def genero(genero: String)=
  (nombre:String) => if genero.equalsIgnoreCase("F") then "Doña" + nombre else "Don" + nombre

/*def genero(genero:String)*/

val squareSum = (x : Int, y : Int) => (x*x + y*y)
squareSum(1, 2)

def add1(x: Double): Double = x +1
val sum1 = add1(_)
sum1(4)

def prodPlus1(x: Int, y : Int): Int = x*y + 1
val mulSum = prodPlus1(_, _)

def tamanio(lista: List[Double]): Int = lista.length
val len = tamanio(_)

//funcion que recibe funcion como parametro

def execute(f : (Int, Int) => Int, a: Int, b: Int ) = f(a,b)

// como seria una funcion que se pueda enviar como parametro a la funcion execute

val sumar = (a : Int, b: Int) => a +b
execute(sumar, 4,5)

def resta(x: Int, y: Int): Int = x - y
execute(resta, 10, 3)
//fucion que devuelve otra funcion

def createAdder(x: Int): Int => Int = (y: Int) => x + y
val add5 = createAdder(5)
print(add5(10))
//def createAdder( x: Int): Int => Int = y => x+y
// val add5: Int => = createAdder(5)
//print(add(10))

def greetingI18N(lan: String): String => String = name =>
val english = () => "Hello, " + name
val spanish = () => "Hola, " + name
lan match
case "en" => english()
case "es" => spanish()
case _ => english()

val greetingEn = greetingI18N("en")
greetingEn("Jorge")

val greetingEs = greetingI18N("es")
greetingEs("Jorge")



