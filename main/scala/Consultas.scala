package ec.edu.utpl.presencial.computacion.pfr.pintegra

import cats.*
import cats.effect.*
import cats.effect.unsafe.implicits.global
import doobie.*
import doobie.implicits.*

object Consultas {

  val xa = Transactor.fromDriverManager[IO](
    driver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://localhost:3306/ProyectoIntegralPFR",
    user = "root",
    password = "#23@luis2002",
    logHandler = None)

  def golesXjugador() = {
    sql"SELECT player_id, count(goal_id) from goals group by 1"
      .query[(String, Double)]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }

  //Consulta para obtener el promedio de la capacidad de los estadios
  def promedioCapacidadEstadios() = {
    sql"SELECT stadium_name, AVG(stadium_capacity) FROM stadiums group by 1"
      .query[(String, Double)]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }

  //Numero de estadios que tiene un pais

  def numEstadioPais() = {
    sql"""select distinct city_name, count(stadium_name) from stadiums group by 1"""
      .query[(String, Double)]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }

  // Número promedio de equipos por torneo
  def promedioEquiposPorTorneo(): IO[Double] = {
    sql"SELECT AVG(count_teams) FROM tournaments"
      .query[Double]
      .unique
      .transact(xa)
  }

  // Número total de jugadores
  def contarTotalJugadores(): IO[Int] = {
    sql"SELECT COUNT(*) FROM players"
      .query[Int]
      .unique
      .transact(xa)
  }

  // Número total de partidos
  def contarTotalPartidos(): IO[Int] = {
    sql"SELECT COUNT(*) FROM matches"
      .query[Int]
      .unique
      .transact(xa)
  }


  // Promedio de goles marcados por jugador
  def promedioGolesPorJugador(): IO[Double] = {
    sql"SELECT AVG(player_id) FROM goals"
      .query[Double]
      .unique
      .transact(xa)
  }

  // Consulta para contar el número total de goles marcados en un torneo específico
  def contarGolesPorTorneo(tournamentId: String): IO[Int] = {
    sql"SELECT COUNT(*) FROM goals INNER JOIN matches ON goals.match_id = matches.match_id WHERE matches.tournament_id = $tournamentId"
      .query[Int]
      .unique
      .transact(xa)
  }
  
}
