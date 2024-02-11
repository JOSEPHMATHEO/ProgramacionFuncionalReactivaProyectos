package ec.edu.utpl.presencial.computacion.pfr.pintegra

// Libreria para leer los csv

import com.github.tototoshi.csv._
import com.github.tototoshi.csv.CSVReader

// Libreria para crear Graficas 

import org.nspl._
import org.nspl.awtrenderer._
import org.nspl.data.HistogramData

import org.saddle.{Index, Series, Vec}

// Libreria para
import cats._
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._


// Libreria para concectar a una Base de Datos 
import doobie.*
import doobie.implicits.*

import java.io.File

object ProyectoIntegradorBimestral {

  val pathDataFile: String = "C:/Users/sucol/OneDrive/Escritorio/dsPartidosYGoles.csv"
  val reader = CSVReader.open(new File(pathDataFile))
  val contentFile: List[Map[String, String]] = reader.allWithHeaders()

  reader.close()

  val pathDataFile2: String = "C:/Users/sucol/OneDrive/Escritorio/dsAlineacionesXTorneo-2.csv"
  val reader2 = CSVReader.open(new File(pathDataFile2))
  val contentFile2: List[Map[String, String]] = reader2.allWithHeaders()

  reader2.close()

  val xa = Transactor.fromDriverManager[IO](
    driver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://localhost:3306/ProyectoIntegralPFR",
    user = "root",
    password = "#23@luis2002",
    logHandler = None)

  @main
  def exportFunc1() =
    
    // Remplazar los datos NA en un String

    def defaultValueString(text: String): String =
      if (text.equals("NA")) {
        ""
      } else {
        text
      }

    // Remplazar los datos NA en un Int

    def defaultValueInt(text: String): Double =
      if (text.equals("NA")) {
        0
      } else {
        text.toDouble
      }

    generateDataGoals(contentFile)
    
    def generateDataGoals(data: List[Map[String, String]]) =
        val sqlInsert = s"INSERT INTO goals(goal_id, team_id, player_id, player_team_id, tournament_id, minute_label, minute_regulation, minute_stoppage, match_period, own_goal, penalty) VALUES('%s', '%s', '%s', '%s', '%s', '%s', %d, %d, '%s', %d, %d);"
        val goalsTuple = data
          .map(
            row => (row("goals_goal_id").trim,
              defaultValueString(row("goals_team_id")),
              defaultValueString(row("goals_player_id")),
              defaultValueString(row("goals_player_team_id")),
              defaultValueString(row("matches_tournament_id")),
              defaultValueString(row("goals_minute_label")),
              defaultValueInt(row("goals_minute_regulation")).toInt,
              defaultValueInt(row("goals_minute_stoppage")).toInt,
              defaultValueString(row("goals_match_period")),
              defaultValueInt(row("goals_own_goal")).toInt,
              defaultValueInt(row("goals_penalty")).toInt
            )
          ).distinct
          .map(t11 => sqlInsert.formatLocal(java.util.Locale.US, t11._1, t11._2, t11._3, t11._4, t11._5, t11._6, t11._7, t11._8, t11._9, t11._10, t11._11))
        goalsTuple.foreach(println)

    generateDataMatches(contentFile)
    def generateDataMatches(data: List[Map[String, String]]) =
      val sqlInsert = s"INSERT INTO matches(match_id, tournament_id, away_team_id, home_team_id, stadium_id, match_date, match_time, stage_name, home_team_score, away_team_score, extra_time, penalty_shootout, home_team_score_penalties, away_team_score_penalties, result) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d, %d, %d, %d, %d, %d, '%s');"
      val matchesTuple = data
        .map(
          row => (row("matches_match_id").trim,
            defaultValueString(row("matches_tournament_id")),
            defaultValueString(row("matches_away_team_id")),
            defaultValueString(row("matches_home_team_id")),
            defaultValueString(row("matches_stadium_id")),
            defaultValueString(row("matches_match_date")),
            defaultValueString(row("matches_match_time")),
            defaultValueString(row("matches_stage_name")),
            defaultValueInt(row("matches_home_team_score")).toInt,
            defaultValueInt(row("matches_away_team_score")).toInt,
            defaultValueInt(row("matches_extra_time")).toInt,
            defaultValueInt(row("matches_penalty_shootout")).toInt,
            defaultValueInt(row("matches_home_team_score_penalties")).toInt,
            defaultValueInt(row("matches_away_team_score_penalties")).toInt,
            defaultValueString(row("matches_result"))
          )
        ).distinct
        .map(t15 => sqlInsert.formatLocal(java.util.Locale.US, t15._1, t15._2, t15._3, t15._4, t15._5, t15._6, t15._7, t15._8, t15._9, t15._10, t15._11, t15._12, t15._13, t15._14, t15._15))
      matchesTuple.foreach(println)

    // Insersion de estadios a traves del csv

    //generateDataManipulateStadium(contentFile).foreach(insert => insert.run.transact(xa).unsafeRunSync())

    // Insersion de teams a traves del csv

    //generateDataManipulateTeams(contentFile).foreach(insert => insert.run.transact(xa).unsafeRunSync())

    // Insersion de tournaments a traves del csv

    //generateDataManipulateTournaments(contentFile).foreach(insert => insert.run.transact(xa).unsafeRunSync())

    def generateDataManipulateStadium(data: List[Map[String, String]]) =
      val stadiumTuple = data
        .map(
          row => (row("matches_stadium_id").trim,
            defaultValueString(row("stadiums_stadium_name")),
            defaultValueString(row("stadiums_city_name")),
            defaultValueString(row("stadiums_country_name")),
            defaultValueInt(row("stadiums_stadium_capacity")).toInt
          )
        ).distinct
        .map(t5 => sql"INSERT INTO stadiums(stadium_id, stadium_name, city_name, country_name, stadium_capacity) VALUES(${t5._1}, ${t5._2}, ${t5._3}, ${t5._4}, ${t5._5})".update)

      stadiumTuple

    def generateDataManipulateTeams(data: List[Map[String, String]]) =
      val teamsTuple = data
        .map(
          row => (row("matches_away_team_id").trim,
            defaultValueString(row("away_team_name")),
            defaultValueInt(row("away_mens_team")).toInt,
            defaultValueInt(row("away_womens_team")).toInt,
            defaultValueString(row("away_region_name"))
          )
        ).distinct
        .map(t5 => sql"INSERT INTO teams(team_id, team_name, mens_team, womens_team, region_name) VALUES(${t5._1}, ${t5._2}, ${t5._3}, ${t5._4}, ${t5._5})".update)

      teamsTuple

    def generateDataManipulateTournaments(data: List[Map[String, String]]) =
      val tournamentsTuple = data
        .map(
          row => (row("matches_tournament_id").trim,
            defaultValueString(row("tournaments_tournament_name")),
            defaultValueInt(row("tournaments_year")).toInt,
            defaultValueString(row("tournaments_host_country")),
            defaultValueString(row("tournaments_winner")),
            defaultValueInt(row("tournaments_count_teams")).toInt
          )
        ).distinct
        .map(t6 => sql"INSERT INTO tournaments(tournament_id, tournament_name, tournaments_year, host_country, winner, count_teams) VALUES(${t6._1}, ${t6._2}, ${t6._3}, ${t6._4}, ${t6._5}, ${t6._6})".update)

      tournamentsTuple

}
