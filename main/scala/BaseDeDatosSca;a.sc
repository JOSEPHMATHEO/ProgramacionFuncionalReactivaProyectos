import doobie._
import doobie.implicits._

import cats._
import cats.effect._

import cats.effect.unsafe.implicits.global

case class Actor(

                  id: Int,
                  name: String,
                  lastname: String

                )

val xa = Transactor.fromDriverManager[IO](

  driver = "com.mysql.cj.jdbc.Driver",
  url = "jdbs:mysql://localhost:3306/sakila",
  user = "root",
  password = "#23@luis2002",
  logHandler = None
)

def find(id:Int): ConnectionIO[Option[Actor]] =

  sql"SELECT a.actor_id, a.first_name, a.last_name FROM actor a where a.actor_id = $id"
    .query[Actor]
    .option

val result = find(1)
  .transact(xa)
  .unsafeRunSync()

println(result.get)

def listAllActors(): ConnectionIO[List[Actor]] =

  sql"SELECT a.actor_id, a.first_name, a.last_name FROM actor a"
    .query[Actor]
    .to[List]

val actorList: List[Actor] = listAllActors()
  .transact(xa)
  .unsafeRunSync()

actorList.foreach(println)

def avgLength(): ConnectionIO[Double] =
  sql"""
     |SELECT avg(f.length) as avg_length
     |FROM film f
     |""".stripMargin
    .query[Double]
    .unique

val lengthAvg: Double = avgLength()
  .transact(xa)
  .unsafeRunSync()

println(lengthAvg)

