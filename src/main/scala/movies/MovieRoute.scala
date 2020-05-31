package movies

import authentication.AuthenticationDirectives._
import models.{LoginRequest, Movie, MovieReview}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import utils.MarshallFormatImplicits

import scala.concurrent.Future

class MovieRoute(movieService: MovieService) extends Directives with  MarshallFormatImplicits {


  private def findMovies: Route = ???

  def getListMovies: Route = get {

    authenticated { _  =>
      pathPrefix("movies") {
        val movieList = movieService.getAllMovies()
        onSuccess(movieList) {
          res => complete(res)
        }
      }
      //      ~
      //        pathPrefix("movie" / LongNumber) { id =>
      //          val movie = dbInstance.findById(id.toString)
      //          onSuccess(movie) {
      //            case Some(res) => complete(res)
      //            case None => complete(StatusCodes.NotFound)
      //          }
      //        }
    }
  }

  def submitMovieReview: Route = post {
    authenticated { authResult =>
      print("DEBUG: | " + authResult)
      pathPrefix("submitreview") {
        entity(as[MovieReview]) { user =>
          println(user)
          movieService.insertMovieReview(user)
          complete(StatusCodes.OK)
        }
      }
    }
  }

}
