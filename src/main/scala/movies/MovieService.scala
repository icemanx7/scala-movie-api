package movies

import models.{Movie, MovieReview, Review}
import reviews.ReviewsRepository

import scala.concurrent.{ExecutionContext, Future}

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository)(implicit executionContext: ExecutionContext) {

  def getAllMovies(): Future[List[Movie]] = {
    movieDbInstance.getAll
  }

  //TODO: Add the review exist
  def doesReviewExist = ???

  def insertMovieReview(movieReview: MovieReview): Future[Any] = {
    val review = Review(None, movieReview.review,movieReview.rating, movieReview.reviewDate)
    reviewDbInstance.insertReview(review)
  }
}
