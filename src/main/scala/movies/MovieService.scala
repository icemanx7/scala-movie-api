package movies

import models.{DetailedMovie, JWTContent, MovieReview, Movies, Review }
import pdi.jwt.JwtClaim
import reviews.ReviewsRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}
import spray.json._
import utils.MarshallFormatImplicits

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository, metaMovieDb: MovieMongoRepository)(implicit executionContext: ExecutionContext) extends MarshallFormatImplicits{

  def getAllMovies(jwtClaim: Try[JwtClaim]): Future[Movies] = {
    jwtClaim match {
      case Success(user) => {
        val currentUser = user.content.parseJson
        val currentUserObj  = currentUser.convertTo[JWTContent]
        movieDbInstance.getAll(currentUserObj.username)
      }
    }
  }

  def getAllMoviesMetaData(): Future[Seq[DetailedMovie]] = {
    metaMovieDb.getAllMovieMetaData()
  }

  def insertMovieReview(movieReview: MovieReview): Future[Int] = {
    val review = Review(None, movieReview.doesLikeMovie)
    reviewDbInstance.insertReview(review, movieReview.username, movieReview.movieID)
  }
}
