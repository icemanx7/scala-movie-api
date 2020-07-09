package movies

import models.{Movie, Review, User}
import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object MoviesMockDB {

  private val db = Database.forConfig("movies")
  private val movies = TableQuery[MoviesDTO]
  private val action: DBIO[Unit] = movies.schema.create
  private lazy val future: Future[Unit] = db.run(action)
  private val result = Await.result(future, 2.seconds)


  def freshTestData = Seq(
    Movie("0", "Benny", "2006"),
    Movie("1", "The game movie", "1996"),
    Movie("2", "What Movie is this", "2001"),
    Movie("3", "BBT", "1989"),
    Movie("4", "THe poeple remembers", "4500"),
    Movie("5", "What Now", "2005"),
  )

  val insert: DBIO[Option[Int]] = movies ++= freshTestData
  val insertAction: Future[Option[Int]] = db.run(insert)
  val rowCount = Await.result(insertAction, 2.seconds)

}
