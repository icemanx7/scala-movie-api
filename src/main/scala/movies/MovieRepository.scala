package movies

import models.{Movie, MovieDTO}
import reviews.{MovieUserReviewDTO, ReviewDTO}
import slick.jdbc.SQLiteProfile.api._
import user.UserDTO

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class MoviesDTO(tag: Tag) extends Table[Movie](tag, "Movies") {
  def id = column[String]("ID", O.PrimaryKey)
  def title = column[String]("Name")
  def year = column[String]("Year")
  def * = (id, title, year) <> (Movie.tupled, Movie.unapply)
}

class MovieRepository (implicit executionContext: ExecutionContext) {

  val movies = TableQuery[MoviesDTO]
  val userTable = TableQuery[UserDTO]
  val reviewTable = TableQuery[ReviewDTO]
  val movieReviewTable = TableQuery[MovieUserReviewDTO]

  val db = Database.forConfig("movies")

  //TODO: Clean up the function
  def getAll(username: String): Future[List[MovieDTO]] = {
    val movieListReview = movies.joinLeft(movieReviewTable).on(_.id === _.movieID).joinLeft(userTable).on((movie, rev) => rev.email === username)
    val newMovieList = db.run(movieListReview.to[List].result)
    newMovieList.map(movieList => {
      movieList.map(singleMovie => {
        val  reviewCompItem = singleMovie._1._2
        val user = singleMovie._2
        val doesHaveReview = for {
          reviewItem <- reviewCompItem
          userItem <- user
        } yield (reviewItem, userItem)
        doesHaveReview match {
          case Some(reviewCompUser) => {
            if (reviewCompUser._2.id == reviewCompUser._1.userID) {
              val movieDetails = singleMovie._1._1
              MovieDTO(movieDetails.id, movieDetails.title, movieDetails.year, true)
            }
            else {
              val movieDetails = singleMovie._1._1
              MovieDTO(movieDetails.id, movieDetails.title, movieDetails.year, false)
            }
          }
          case None => {
            val movieDetails = singleMovie._1._1
            MovieDTO(movieDetails.id, movieDetails.title, movieDetails.year, false)
          }
        }
      })

    })
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
