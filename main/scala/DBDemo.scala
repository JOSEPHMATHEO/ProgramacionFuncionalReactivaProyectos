package ec.edu.utpl.presencial.computacion.pfr.pintegra

import doobie._
import doobie.implicits._

import cats._
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._



// Creacion de las case class

// Case class Actor

case class Actor(

                  id: Int,
                  name: String,
                  lastname: String

                )

// Case class Film

case class Film(

                 id: Int,
                 tittle: String,
                 releaseYear: Int,
                 actorList: String,

               )

case class CategoryFilm(

                 name: String,
                 category: String,

               )

case class FilmData(
                     id: Int,
                     title: String,
                     releaseYear: Int,
                     actorList: List[Actor],
                     language: String
                   )

object DBDemo{
  @main def demo(): Unit =
    println("Demo")

    val xa = Transactor.fromDriverManager[IO](

      driver = "com.mysql.cj.jdbc.Driver",
      url = "jdbc:mysql://localhost:3306/sakila",
      user = "root",
      password = "#23@luis2002",
      logHandler = None

    )

    // Scripts

    val result: Option[Actor] = find(1)
      .transact(xa)
      .unsafeRunSync()

    println(result.get)

    val actorList: List[Actor] = listAllActors()
      .transact(xa)
      .unsafeRunSync()

    actorList.foreach(println)

    val filmList: List[Film] = listOfFilms()
      .transact(xa)
      .unsafeRunSync()

    filmList.foreach(println)

    val tittleC: List[CategoryFilm] = tittleCategory()
      .transact(xa)
      .unsafeRunSync()

    tittleC.foreach(println)

    println("Movie data")

    val movieData = findFilmDataById(2)
      .transact(xa)
      .unsafeRunSync()
    println(movieData.get)

// Funciones

def find(id: Int): ConnectionIO[Option[Actor]] =

  sql"SELECT a.actor_id, a.first_name, a.last_name FROM actor a where a.actor_id = $id"
    .query[Actor]
    .option

def listAllActors(): ConnectionIO[List[Actor]] =

  sql"SELECT a.actor_id, a.first_name, a.last_name FROM actor a"
    .query[Actor]
    .to[List]

def listOfFilms(): ConnectionIO[List[Film]] =

  sql"""
        SELECT f.film_id, f.title, f.release_year, group_concat(CONCAT(a.first_name, ' ', a.last_name)) as actor_list
        FROM film f, film_actor fa, actor a
        WHERE f.film_id = fa.film_id
        AND fa.actor_id = a.actor_id
        GROUP BY f.film_id, f.title
        """
    .query[Film]
    .to[List]
def tittleCategory(): ConnectionIO[List[CategoryFilm]] =

  sql"""
        SELECT f.title, c.name AS category_name
        FROM film f, category c, film_category fc
        WHERE f.film_id = fc.film_id
        AND c.category_id = fc.category_id
        ORDER BY f.title
        """
    .query[CategoryFilm]
    .to[List]

// Fase 2

  def findFilmDataById(filmId: Int) =

    def findFilmById(): ConnectionIO[Option[(Int, String, Int, Int)]] =

      sql"""
           |SELECT f.film_id, f.title, f.release_year, f.language_id
           |FROM film f
           |WHERE f.film_id = $filmId
           |""".stripMargin
        .query[(Int, String, Int, Int)]
        .option

    def findLanguageById(languageId: Int): ConnectionIO[String] =
      sql"""
           |SELECT l.name
           |FROM language l
           |WHERE l.language_id = $languageId
           |"""
        .stripMargin
        .query[String]
        .unique

    def findActorsByFilmId(filmId: Int) =
      sql"""
           |SELECT a.actor_id, a.first_name, a.last_name
           |FROM actor a
           |JOIN film_actor fa on a.actor_id = fa.actor_id
           |WHERE fa.film_id = $filmId
           |"""
        .stripMargin
        .query[Actor]
        .to[List]

    val query = for {

      optFilm <- findFilmById()
      language <- optFilm match {
        case Some(film) => findLanguageById(film._4)
        case None => "".pure[ConnectionIO]
      }

      actors <- optFilm match {
        case Some(film) => findActorsByFilmId(film._1)
        case None => List.empty[Actor].pure[ConnectionIO]
      }

    } yield {
      optFilm.map { film =>
        FilmData(film._1, film._2, film._3, actors, language)
      }
    }
    query

}