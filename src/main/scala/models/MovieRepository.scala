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

class Movies(tag: Tag) extends Table[(String, String, String)](tag, "MovieTitle") {
  def id = column[String]("ID", O.PrimaryKey)
  def title = column[String]("Name")
  def year = column[String]("Year")
  def * = (id, title, year)
}

class MovieRepository {
  val movies = TableQuery[Movies]

  val db = Database.forConfig("movies")

  def getAll(): Future[List[(String, String, String)]] = {
    db.run(movies.to[List].result)
  }

}
