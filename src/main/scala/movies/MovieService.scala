package movies

import models.{JWTContent, Movie, MovieDTO, MovieReview, Movies, Review, ReviewCompDTO, ReviewExist}
import pdi.jwt.JwtClaim
import reviews.ReviewsRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}
import spray.json._
import DefaultJsonProtocol._
import utils.MarshallFormatImplicits

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository)(implicit executionContext: ExecutionContext) extends MarshallFormatImplicits{

  def getAllMovies(jwtClaim: Try[JwtClaim]): Future[Movies] = {
    jwtClaim match {
      case Success(user) => {
        val currentUser = user.content.parseJson
        val currentUserObj  = currentUser.convertTo[JWTContent]
        movieDbInstance.getAll(currentUserObj.username)
      }
    }
  }

  def insertMovieReview(movieReview: MovieReview): Future[Int] = {
    val review = Review(None, movieReview.review,movieReview.rating, movieReview.reviewDate)
    reviewDbInstance.insertReview(review, movieReview.username, movieReview.movieID)
  }
}
