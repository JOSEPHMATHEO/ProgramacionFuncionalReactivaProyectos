package ec.edu.utpl.presencial.computacion.pfr.pintegra

import com.github.tototoshi.csv.*
import org.nspl.*
import org.nspl.awtrenderer.*
import org.nspl.data.HistogramData
import org.saddle.{Index, Series, Vec}

import com.github.tototoshi.csv.*
import java.io.File

implicit object CustomFormat extends DefaultCSVFormat{
  override val delimiter: Char = ';'
}

object Proyecto {

  val path2DataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsPartidosYGoles.csv"// Ruta de ubicacion del dataset
  val reader = CSVReader.open(new File(path2DataFile))
  val contentFile: List[Map[String, String]] = reader.allWithHeaders()
  reader.close() // Se cierra el archivo

  val path2DataFile2: String = "C:/Users/sucol/OneDrive/Escritorio/dsAlineacionesXTorneo.csv"// Ruta de ubicacion del dataset
  val reader2 = CSVReader.open(new File(path2DataFile2))
  val contentFile2: List[Map[String, String]] = reader2.allWithHeaders()
  reader.close() // Se cierra el archivo

  @main
  def work() =

    val path2DataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsPartidosYGoles.csv" // Ruta de ubicacion del dataset
    val reader = CSVReader.open(new File(path2DataFile)) // Se abre el archivo
    val contentFile: List[Map[String, String]] = reader.allWithHeaders()

    reader.close() // Se cierra el archivo

    // Total de Filas y Columnas del dataset dsPartidosYGoles.csv

    println(s"Numero de Filas y Columnas del dataset PartidosYGoles" +
      s"\nFilas: ${contentFile.length}"+
      s"\nColumnas: ${contentFile(0).keys.size}")

    //Maxima Capcidad
    println(
      contentFile
        .map(x => x("stadiums_stadium_name") -> x("stadiums_stadium_capacity"))
        .distinct
        .maxBy(_._2.toInt)
    )
    //Minima Capacidad
    println(
      contentFile
        .map(x => x("stadiums_stadium_name") -> x("stadiums_stadium_capacity"))
        .distinct
        .minBy(_._2.toInt)
    )
    //Promedio Capacidad
    println(
      contentFile
        .map(x => x("stadiums_stadium_name") -> x("stadiums_stadium_capacity"))
        .distinct
        .map(_._2.toDouble)
        .sum
        /
        contentFile
          .map(x => x("stadiums_stadium_name") -> x("stadiums_stadium_capacity"))
          .distinct
          .length

    )
    //Hombres
    println(
      contentFile
        .filter(x => x("tournaments_tournament_name").contains("Men"))
        .filter(_("goals_minute_regulation") != "NA")
        .map(x => x("goals_minute_regulation"))
        .groupBy(identity)
        .map(x => x._1 -> x._2.length)
        .maxBy(_._2)
    )
    //Mujeres
    println(
      contentFile
        .filter(x => x("tournaments_tournament_name").contains("Women"))
        .filter(_("goals_minute_regulation") != "NA")
        .map(x => x("goals_minute_regulation"))
        .groupBy(identity)
        .map(x => x._1 -> x._2.length)
        .maxBy(_._2)
    )

    //Periodo mas Comun
    println(
      contentFile
        .filter(_("goals_match_period") != "NA")
        .map(x => x("goals_match_period"))
        .groupBy(identity)
        .map(x => x._1 -> x._2.length)
        .maxBy(_._2)


    )

  @main
  def work2() =

    val path2DataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsAlineacionesXTorneo.csv"
    val reader2 = CSVReader.open(new File(path2DataFile))
    //val contentFile: List[List[String]] = reader.all()
    val contentFile: List[Map[String, String]] = reader2.allWithHeaders()

    reader2.close()

    // Total de Filas y Columnas del dataset dsAlineacionesXTorneo.csv

    println(s"Numero de Filas y Columnas del dataset AlineacionesXTorneo" +
      s"\nFilas: ${contentFile.length}" +
      s"\nColumnas: ${contentFile(0).keys.size}")

    //Comun Camiseta
    println(
      contentFile
        .map(x => x("squads_shirt_number") -> x("squads_position_name"))
        .groupBy(_._1)
        .map(x => x._1 -> x._2.length)
        .maxBy(_._2)
    )

    // -- Estadísticas Generales: --
    // -- Número total de jugadores y equipos: --

    val numeroTotalJugadores: Int = contentFile.map(row => row("squads_player_id")).distinct.length
    val numeroTotalEquipos: Int = contentFile.map(row => row("squads_team_id")).distinct.length
    println(s"Número total de jugadores: $numeroTotalJugadores")
    println(s"Número total de equipos: $numeroTotalEquipos")


    // --Porcentaje de jugadores masculinos y femeninos:-- //

    val porcentajeMasculinos: Double = contentFile.count(row => row("players_female") == "0").toDouble / numeroTotalJugadores * 100
    val porcentajeFemeninos: Double = contentFile.count(row => row("players_female") == "1").toDouble / numeroTotalJugadores * 100
    println(s"Porcentaje de jugadores masculinos: $porcentajeMasculinos%")
    println(s"Porcentaje de jugadores femeninos: $porcentajeFemeninos%")


    // -- Número de camiseta (squads_shirt_number) más común que se utiliza en cada una de las posiciones --

    val agrupacionPosicionCamiseta: Map[String, List[Int]] =
      contentFile.groupBy(x => x("squads_position_name")).map((posicion, filas) =>
        posicion -> filas.map(x => x(("squads_shirt_number")).toInt))

    val frecuenciasPorPosicion: Map[String, Map[Int, Int]] = agrupacionPosicionCamiseta.map((posicion, numerosCamiseta) =>
      posicion -> numerosCamiseta.groupBy(identity).map((numCamiseta, listaNumeros) =>
        numCamiseta -> listaNumeros.size))

    // Obtener el número de mayor frecuencia por cada posición
    val maxFrecuenciaPorPosicion: Map[String, Tuple2[Int, Int]] = frecuenciasPorPosicion.map((posicion, frecuencias) =>
      val maxEntry = frecuencias.maxBy(_._2)
      posicion -> maxEntry
    )

    println(maxFrecuenciaPorPosicion)

  @main
  def work3() =

    val path2DataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsAlineacionesXTorneo.csv"
    val reader = CSVReader.open(new File(path2DataFile))
    //val contentFile: List[List[String]] = reader.all()
    val contentFile: List[Map[String, String]] = reader.allWithHeaders()

    reader.close()

    //println(contentFile.take(1)(0)("tournaments_winner"))
    charting(contentFile)

    def charting(data: List[Map[String, String]]): Unit =

      val listNroShirt: List[Double] = data
        .filter(row => row("squads_position_name") == "forward" && row("squads_shirt_number") != "0")
        .map(row => row("squads_shirt_number").toDouble)

      val histForwardShirtNumber = xyplot(HistogramData(listNroShirt, 20) -> bar())(

        par
          .xlab("Shirt number")
          .ylab("freq.")
          .main("Forward shirt number")

      )

      pngToFile(new File("C:/Users/sucol/OneDrive/Escritorio/Imagenes Histograma/fhsn2.png"),histForwardShirtNumber.build, 1000)
      renderToByteArray(histForwardShirtNumber.build, width = 2000)


// ================================================================================================================================================

// Trabajo Grupal PFR

  @main

  // ================================ 1 Parte ================================

  def work4() =

    val path2DataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsPartidosYGoles.csv"
    val reader = CSVReader.open(new File(path2DataFile))
    //val contentFile: List[List[String]] = reader.all()
    val contentFile: List[Map[String, String]] = reader.allWithHeaders()

    reader.close()

    //println(contentFile.take(1)(0)("tournaments_winner"))
    charting(contentFile)

    def charting(data: List[Map[String, String]]): Unit =

      val listGoalRegul: List[Double] = data
        .filter(row => row("goals_minute_regulation") != "NA") // Filtrado de datos vacios
        .map(row => row("goals_minute_regulation").toDouble) // convercion de String a Real

      val histForwardGoalxMinute = xyplot(HistogramData(listGoalRegul, 20) -> bar())(

        par
          .xlab("Minutes")
          .ylab("freq.")
          .main("Goals x Minute")

      )

      pngToFile(new File("C:/Users/sucol/OneDrive/Escritorio/Imagenes Histograma/fhsn5.png"),histForwardGoalxMinute.build, 1000)

    // ================================ 2 Parte ================================

    def datosGrafica(data: List[Map[String, String]]) : List[(String, Int)] =
      val dataGoles = data
        .filter(_("tournaments_tournament_name").contains("Women"))
        .map(row => (
          row("tournaments_tournament_name"),
          row("matches_match_id"),
          row("matches_home_team_score"),
          row("matches_away_team_score")
        ))
        .distinct
        .map(t4=>(t4._1, t4._3.toInt + t4._4.toInt))
        .groupBy(_._1)
        .map(t2 => (t2._1, t2._2.map(_._2).sum))
        .toList
        .sortBy(_._1)
      dataGoles


    // ================================ 3 Parte ================================

    def chartBarPlot(data: List[(String, Int)]) : Unit =

      val data4Chart: List[(String, Double)] = data
        .map(t2 => (t2._1, t2._2.toDouble))

      val indices = Index(data4Chart.map(value => value._1.substring(0,4)).toArray)
      val values = Vec(data4Chart.map(value => value._2).toArray)

      val series = Series(indices, values)

      val bar1 = saddle.barplotHorizontal(series,
        xLabFontSize = Option(RelFontSize(1)),
        color = RedBlue(86, 146))(
        par
          .xLabelRotation(-77)
          .xNumTicks(0)
          .xlab("Years")
          .ylab("freq.")
          .main("Tournaments"))


      pngToFile(new File("C:/Users/sucol/OneDrive/Escritorio/Imagenes Histograma/trabajoo2.png"), bar1.build, 1000)

    def datosGrafica2(data: List[Map[String, String]]): List[(String, Int)] =
      val dataGoles = data
        .filter(_("tournaments_tournament_name").contains("Men"))
        .map(row => (
          row("tournaments_tournament_name"),
          row("matches_match_id"),
          row("matches_home_team_score"),
          row("matches_away_team_score")
        ))
        .distinct
        .map(t4 => (t4._1, t4._3.toInt + t4._4.toInt))
        .groupBy(_._1)
        .map(t2 => (t2._1, t2._2.map(_._2).sum))
        .toList
        .sortBy(_._1)
      dataGoles

    chartBarPlot(datosGrafica(contentFile))
    chartBarPlot(datosGrafica2(contentFile))
}