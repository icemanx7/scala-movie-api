package movies

import models.Movie

import scala.concurrent.{ExecutionContext, Future}

class MovieService (dbInstance: MovieRepository) (implicit executionContext: ExecutionContext) {

  def getAllMovies(): Future[List[Movie]] = {
    dbInstance.getAll
  }

  def postMovieReview() = {
    dbInstance.insertReview()
  }

}
