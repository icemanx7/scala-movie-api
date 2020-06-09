package movies

import authentication.AuthenticationDirectives._
import models.{MovieReview}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.StatusCodes
import utils.MarshallFormatImplicits


class MovieRoute(movieService: MovieService) extends Directives with  MarshallFormatImplicits {

  def getMovieEntities: Route = get {

    authenticated { _  =>
      pathPrefix("movies") {
        val movieList = movieService.getAllMovies()
        onSuccess(movieList) {
          res => complete(res)
        }
      } ~
        pathPrefix("reviewable") {
          val movieList = movieService.getAllMovies()
          onSuccess(movieList) {
            res => complete(res)
          }
        }
    }
  }

  def submitMovieReview: Route = post {
    authenticated { authResult =>
      pathPrefix("submitreview") {
        entity(as[MovieReview]) { review =>
          movieService.insertMovieReview(review)
          complete(StatusCodes.OK)
        }
      }
    }
  }

}
