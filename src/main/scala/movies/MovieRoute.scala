package movies

import authentication.AuthenticationDirectives._
import models.{InsertResp, MovieReview, ReviewCompDTO}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.StatusCodes
import utils.MarshallFormatImplicits

import scala.concurrent.ExecutionContext

//TODO: Maybe make you're own response types so you can manually set the correct messages and response codes.

class MovieRoute(movieService: MovieService)(implicit executionContext: ExecutionContext) extends Directives with  MarshallFormatImplicits {

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
          val insertResult = movieService.insertMovieReview(review)
          val insertResultDto = insertResult.map(item => {
            InsertResp(200, item.toString)
          })
          complete(insertResultDto)
        }
      }
    }
  }

}
