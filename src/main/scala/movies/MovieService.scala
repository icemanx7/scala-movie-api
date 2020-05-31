package movies

import models.{Movie, MovieReview, Review}

import scala.concurrent.{ExecutionContext, Future}

class MovieService(movieDbInstance: MovieRepository, reviewDbInstance: ReviewsRepository )(implicit executionContext: ExecutionContext) {

  def getAllMovies(): Future[List[Movie]] = {
    movieDbInstance.getAll
  }

  def insertMovieReview(movieReview: MovieReview): Future[Unit] = {
    val review = Review(None, movieReview.review,movieReview.rating, movieReview.reviewDate)
    reviewDbInstance.insertReview(review)
  }
}
