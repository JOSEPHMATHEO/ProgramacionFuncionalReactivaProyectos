package ec.edu.utpl.presencial.computacion.pfr.pintegra

import org.nspl.*
import org.nspl.awtrenderer.*
import org.nspl.data.HistogramData
import com.github.tototoshi.csv.*
import org.nspl.{data, *}
import org.saddle.{Index, Series, Vec}

import java.io.File
import scala.collection.immutable.List
import Consultas.*

object Graficas {
  @main
  // Graficos con la Base de Datos
  def golesxJugador() = {
    val dataForChar = Consultas.golesXjugador()

    val indices = Index(dataForChar.map(_._1.substring(3, 7)).toArray)
    val values = Vec(dataForChar.map(_._2).toArray)
    val series = Series(indices, values)
    val bar = saddle.barplotHorizontal(series, xLabFontSize = Option(RelFontSize(1)))(par
      .xLabelRotation(-77)
      .xlab("Jugador")
      .ylab("Goles")
      .main("GolesXJugador"))
    pngToFile(new File("src\\main\\scala\\2golesxJugador.png"), bar.build, 500)

  }

  @main
  def estadioxnombre() = {
    val dataForChar = Consultas.promedioCapacidadEstadios()

    val indices = Index(dataForChar.map(_._1.substring(0, 5)).toArray)
    val values = Vec(dataForChar.map(_._2).toArray)
    val series = Series(indices, values)
    val bar = saddle.barplotHorizontal(series, xLabFontSize = Option(RelFontSize(1)))(par
      .xLabelRotation(-77)
      .xlab("Estadio")
      .ylab("Capacidad")
      .main("Estadio Capacidad"))
    pngToFile(new File("src\\main\\scala\\2estadioXnombre.png"), bar.build, 500)

  }

  @main
  def numeroEstadio() = {
    val dataForChar = Consultas.numEstadioPais()

    val indices = Index(dataForChar.map(_._1.substring(0, 4)).toArray)
    val values = Vec(dataForChar.map(_._2).toArray)
    val series = Series(indices, values)
    val bar = saddle.barplotHorizontal(series, xLabFontSize = Option(RelFontSize(1)))(par
      .xLabelRotation(-77)
      .xlab("Ciudad")
      .ylab("Num Estadios")
      .main("Numero Estadios Ciudad"))
    pngToFile(new File("src\\main\\scala\\2numEstadio.png"), bar.build, 500)

  }

}
