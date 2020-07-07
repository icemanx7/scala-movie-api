package movies

import models.{JWTContent, Movie, MovieReview, Review, ReviewCompDTO, ReviewExist}
import pdi.jwt.JwtClaim
import reviews.ReviewsRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}
import spray.json._
import DefaultJsonProtocol._
import utils.MarshallFormatImplicits

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository)(implicit executionContext: ExecutionContext) extends MarshallFormatImplicits{

  def getAllMovies(jwtClaim: Try[JwtClaim]): Future[List[Movie]] = {
    jwtClaim match {
      case Success(user) => {
        val currentUser = user.content.parseJson
        val currentUserObj  = currentUser.convertTo[JWTContent]
        movieDbInstance.getAll(currentUserObj.username)
      }
    }
  }

  //TODO: Add the review exist
  def doesReviewExist(reviewComp: ReviewCompDTO): Future[ReviewExist] = {
    reviewDbInstance.doesReviewExist(reviewComp)
  }

  def insertMovieReview(movieReview: MovieReview): Future[Any] = {
    val review = Review(None, movieReview.review,movieReview.rating, movieReview.reviewDate)
    reviewDbInstance.insertReview(review)
  }
}
