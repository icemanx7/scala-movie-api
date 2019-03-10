package models

import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.syntax._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api._
import io.circe.syntax._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import cats.effect._
import io.circe._
import io.circe.literal._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._

final case class Movie(id: String, title: String, year: String)

class Movies(tag: Tag) extends Table[Movie](tag, "MovieTitle") {
  def id = column[String]("ID", O.PrimaryKey)
  def title = column[String]("Name")
  def year = column[String]("Year")
  def * = (id, title, year) <> (Movie.tupled, Movie.unapply)
}

class MovieRepository {
  val movies = TableQuery[Movies]

  val db = Database.forConfig("movies")

  def getAll(): Future[List[Movie]] = {
    db.run(movies.to[List].result)
  }

  def findById(id: String): Future[Option[Movie]] = {
    val searchId = movies.filter(_.id === id)
    db.run(searchId.to[List].result.headOption)
  }

  def findByTitle(title: String): Future[List[Movie]] = {
    db.run(movies.to[List].result)
  }

  def search(searchText: String): Future[List[Movie]] = {
    db.run(movies.to[List].result)
  }

  def findByYear(): Future[List[Movie]] = {
    db.run(movies.to[List].result)
  }

}
