package movies

import models.{Movie, MovieReview, Review, ReviewCompDTO, ReviewExist}
import reviews.ReviewsRepository

import scala.concurrent.{ExecutionContext, Future}

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository)(implicit executionContext: ExecutionContext) {

  def getAllMovies(): Future[List[Movie]] = {
    movieDbInstance.getAll
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
