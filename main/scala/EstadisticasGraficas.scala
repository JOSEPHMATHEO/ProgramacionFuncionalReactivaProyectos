package ec.edu.utpl.presencial.computacion.pfr.pintegra

import com.github.tototoshi.csv._

import org.nspl._
import org.nspl.awtrenderer._
import org.nspl.data.HistogramData
import org.saddle.{Index, Series, Vec}

import doobie._
import doobie.implicits._

import cats._
import cats.effect._
import cats.implicits._

import cats.effect.unsafe.implicits.global

import java.io.File

object EstadisticasGraficas {

  // Lectura del archivo PartidosYGoles
  val pathDataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsPartidosYGoles.csv"
  val reader = CSVReader.open(new File(pathDataFile))
  val contentFile: List[Map[String, String]] = reader.allWithHeaders()

  reader.close()

  // Lectura del archivo AlineacionesXTorneo
  val pathDataFile2: String = "C:/Users/sucol/OneDrive/Escritorio/dsAlineacionesXTorneo-20.csv"
  val reader2 = CSVReader.open(new File(pathDataFile2))
  val contentFile2: List[Map[String, String]] = reader2.allWithHeaders()

  reader2.close()

  @main
  def estadisticas() =
    print(resultadosPartidosM(contentFile))
    print(resultadosPartidosW(contentFile))
    print(mediaGolesM(contentFile))
    print(mediaGolesW(contentFile))
    print(horarioMasJugado(contentFile))
    print(horarioMenosJugado(contentFile))

    print(jugadorMasAlineaciones(contentFile2))

  @main
  def graficas() =
    densityMinGoal(contentFile)

    chartBarPlot(datosGrafica(contentFile))
  
  // Victorias locales, visitantes y empates, en los mundiales Masculinos
  def resultadosPartidosM(data: List[Map[String, String]]): String =
    val victoriasLocales: Int = data
      .filter(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("home team win"))

    val victoriasVisitantes: Int = data
      .filter(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("away team win"))

    val empates: Int = data
      .filter(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("draw"))

    s"Mundiales Masculinos\n" +
      s"- Victorias locales: $victoriasLocales\n" +
      s"- Victorias visitantes: $victoriasVisitantes\n" +
      s"- Empates: $empates\n"

  // Victorias locales, visitantes y empates, en los mundiales Femenino
  def resultadosPartidosW(data: List[Map[String, String]]): String =
    val victoriasLocales: Int = data
      .filterNot(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("home team win"))

    val victoriasVisitantes: Int = data
      .filter(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("away team win"))

    val empates: Int = data
      .filter(_("tournaments_tournament_name").contains("Men"))
      .map(row => (row("matches_match_id"),
        row("matches_result")))
      .distinct
      .map(_._2)
      .count(_.equals("draw"))

    s"Mundiales Femenino\n" +
      s"- Victorias locales: $victoriasLocales\n" +
      s"- Victorias visitantes: $victoriasVisitantes\n" +
      s"- Empates: $empates\n"

  // Media de goles en los mundiales masculinos
  def mediaGolesM(data: List[Map[String, String]]): String =
    val golesMundialesM = data
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

    val media: Double = golesMundialesM.map(_._2).sum / golesMundialesM.length.toDouble

    s"\nMedia de goles Mundiales masculinos: $media\n"

  // Media de goles en los mundiales femeninos
  def mediaGolesW(data: List[Map[String, String]]): String =
    val golesMundialesM = data
      .filterNot(_("tournaments_tournament_name").contains("Men"))
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

    val media: Double = golesMundialesM.map(_._2).sum / golesMundialesM.length.toDouble

    s"\nMedia de goles Mundiales femeninos: $media\n"

  // Clave del jugador/a con más convocatorias en mundiales
  def jugadorMasAlineaciones(data: List[Map[String, String]]): String =
    val maxConvocatorias = data
      .map(row => (row("squads_player_id"), row("squads_tournament_id")))
      .groupBy(_._1)
      .map(x => (x._1, x._2.length))
      .maxBy(_._2)

    val cod: String = maxConvocatorias._1
    val convocatorias: Int = maxConvocatorias._2

    s"\nJugador/a con Más Convocatorias:\n" +
      s"- Código jugador: $cod\n" +
      s"- Convocatorias: $convocatorias\n"

  // Horario donde se juegan menos partidos
  def horarioMasJugado(data: List[Map[String, String]]): String =
    val horaMasJugada = data
      .map(row => (row("matches_match_time"), row("matches_match_id")))
      .groupBy(_._1)
      .map(x => (x._1, x._2.length))
      .maxBy(_._2)

    val hora: String = horaMasJugada._1
    val partidosJugados: Int = horaMasJugada._2

    s"\nHorario donde se juegan más partidos:\n" +
      s"- Horario: $hora\n" +
      s"- Partidos jugados: $partidosJugados\n"

  // Horario donde se juegan menos partidos
  def horarioMenosJugado(data: List[Map[String, String]]): String =
    val horaMenosJugada = data
      .map(row => (row("matches_match_time"), row("stadiums_stadium_capacity")))
      .groupBy(_._1)
      .map(x => (x._1, x._2.length))
      .minBy(_._2)

    val hora: String = horaMenosJugada._1
    val partidosJugados: Int = horaMenosJugada._2

    s"\nHorario donde se juegan más partidos:\n" +
      s"- Horario: $hora\n" +
      s"- Partidos jugados: $partidosJugados\n"

  def densityMinGoal(data: List[Map[String, String]]): Unit =
    val listGoalMin: List[Double] = data
      .map(row => row("goals_minute_regulation"))
      .filter(_ != "NA")
      .map(_.toDouble)

    val density1 = xyplot(density(listGoalMin.toIndexedSeq, 10) -> line())(
      par
        .withXLab("Minutos")
        .withYLab("Densidad")
        .withMain("Frecuencia Minutos Goles")
    )

    pngToFile(new File("src\\main\\scala\\grafica1.png"),
      density1.build, 1000)

  def datosGrafica(data: List[Map[String, String]]) =
    val dataHorarios = data
      .map(row => (row("matches_match_time"), row("matches_match_id")))
      .distinct
      .groupBy(_._1)
      .map(x => (x._1, x._2.length))
      .toList
      .sortBy(_._1)
    dataHorarios

  def chartBarPlot(data: List[(String, Int)]): Unit =
    val dataGrafica: List[(String, Double)] = data
      .map(x => (x._1, x._2.toDouble))

    val indices = Index(dataGrafica.map(value => value._1).toArray)
    val values = Vec(dataGrafica.map(value => value._2).toArray)

    val series = Series(indices, values)

    val bar1 = saddle.barplotHorizontal(series,
      xLabFontSize = Option(RelFontSize(0.5)))(par.xLabelRotation(-77).xNumTicks(0)
      .xlab("Hora de Partido")
      .ylab("Numero de Partidos")
      .main("Cantidad de Partidos Jugados por Horario"))

    pngToFile(new File("src\\main\\scala\\grafica2.png"),
      bar1.build, 5000)
}