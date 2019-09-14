package movies

import models.Movie
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Future

class MoviesDTO(tag: Tag) extends Table[Movie](tag, "MovieTitle") {
  def id = column[String]("ID", O.PrimaryKey)
  def title = column[String]("Name")
  def year = column[String]("Year")
  def * = (id, title, year) <> (Movie.tupled, Movie.unapply)
}

class MovieRepository {

  val movies = TableQuery[MoviesDTO]

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
