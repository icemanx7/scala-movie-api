package movies

import models.Movie
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Future

class MoviesDTO(tag: Tag) extends Table[Movie](tag, "Movies") {
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

//  def insertReview(): Unit = {
//    val insertActions = DBIO.seq(
//      movies += Movie("99999", "rrr", "ddd"),
//
////      movies ++= Seq(
////        ("test2", "test2"),
////        (te)
////      ),
//
//      // "sales" and "total" will use the default value 0:
//      movies.map(c => (c.id, c.title, c.year)) += ("Colombian_Decaf", "","")
//    )
//
//    db.run(insertActions)
//  }
}
