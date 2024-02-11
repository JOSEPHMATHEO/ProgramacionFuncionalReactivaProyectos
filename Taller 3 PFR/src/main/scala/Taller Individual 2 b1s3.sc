// 1. Implemente en Scala lo siguiente:

  // La expresión:

        (1 to 10).sum

  // Defina la Funcion:

        def Sumatoria(n: Int): Double =(1 to n).sum * math.pow(n,n-1) - 1

  // Mapee la siguiente:

      val n = (n: Int)=> math.pow((1 to n).sum, 2) - 2 * n

// 2. Implemente la función isPrime, definida así:

    def isPrime(n: Int): Boolean = (2 to (n - 1) ).forall(k => n % k != 0)

// 3. Usted ha sido contratado en una veterinaria para realizar algunos reportes que pueden incluir información de las
//    mascotas o del propietario o de ambos. La información se ha estructurado de la siguiente manera:

  // 1. Cree las estructra de clases (case) para representar la estructra de los datos.

  case class Propietario(nombre: String, edad : Int, genero: String)
  case class Mascota(tipo : String, nombre: String, raza: String, edad : Int, propietario : Propietario)

  // 2. Asigne la lista a un valor (val), pero, debe especificar el tipo de dato de ese valor.

  val mascotas = List(
    Mascota("Gato", "Michi", "Siames", 4, Propietario("María", 18, "F")),
    Mascota("Tortuga", "Rafael", "Terrestre", 80, Propietario("Marco", 45, "M")),
    Mascota("Perro", "Gaudí", "Huski", 5, Propietario("Miguel", 33, "M")),
    Mascota("Perro", "Toby", "Boxer", 7, Propietario("Mirta", 51, "F")),
    Mascota("Gato", "Frufru", "Siberiano", 2, Propietario("Manuel", 23, "M")),
  )

  // 3. Defina una función que permite obtener la mascota de mayor edad.

      def mascotaMaxBy(mascotas: List[Mascota]): Mascota = mascotas.maxBy(_.edad)

  // 4. Cree una función sin nombre que permite obtener la mascota de menor edad. Utilice minBy

  (mascotas: List[Mascota])=> mascotas.minBy(_.edad)

  // 5. Defina una función que permite obtener la mascota que tiene al propietario de mayor edad.

    def mascotaPMaxBy(mascotas: List[Mascota]):Mascota = mascotas.maxBy(_.propietario.edad)

  // 6. Cree una función sin nombre que permite obtener la mascota que tiene al propietario de menor edad.

  (mascotas: List[Mascota]) => mascotas.minBy(_.propietario.edad)

  // 7. Defina una función que responda a la pregunta ¿Todos las mascotas tiene un propietario cuyo nombre inicia con M?

  def nomIniM(mascotas: List[Mascota]):Boolean = mascotas.forall(_.propietario.nombre.startsWith("M"))

  // 8. Cree una función sin nombre que permita ordenar (sortBy) las lista de mascotas por el nombre de la mascota.

  (mascotas: List[Mascota]) => mascotas.sortBy(_.nombre)

  // 9. Defina una función que permita ordenar (sortBy) las lista de mascotas por la edad del propietario.

  def ordenProE(mascotas: List[Mascota]):List[Mascota] = mascotas.sortBy(_.propietario.edad)

  // 10. Cree una función sin nombre que ordene la lista de mascotas por el nombre del propietario

  (mascotas: List[Mascota]) => mascotas.sortBy(_.propietario.nombre)