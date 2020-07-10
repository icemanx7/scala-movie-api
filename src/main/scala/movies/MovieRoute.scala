package movies

import authentication.AuthenticationDirectives._
import models.{MovieReview, ReviewCompDTO}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.StatusCodes
import utils.MarshallFormatImplicits


class MovieRoute(movieService: MovieService) extends Directives with  MarshallFormatImplicits {

  def getMovieEntities: Route = get {

    authenticated { token =>
      println("token: " + token)
      pathPrefix("movies") {
        val movieList = movieService.getAllMovies(token)
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
