import doobie._
import doobie.implicits._

import cats._
import cats.effect._

import cats.effect.unsafe.implicits.global

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
                actorList: String

              )

object DBDemo{
  @main def demo(): Unit =
    println("Demo")

    val xa = Transactor.fromDriverManager[IO](

      driver = "com.mysql.cj.jdbc.Driver",
      url = "jdbs:mysql://localhost:3306/sakila",
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



}