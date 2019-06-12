package movies

import authentication.AuthenticationDirectives._
import models.{Movie, MovieRepository}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import utils.MarshallFormatImplicits._
import scala.concurrent.Future

class MovieRoute(dbInstance: MovieRepository) {


  private def findMovies: Route = ???

  def getListMovies: Route = get {

    authenticated { _  =>
      pathPrefix("movies" ) {
        val movieLists: Future[List[Movie]] = dbInstance.getAll
        onSuccess(movieLists) {
          res => complete(res)
        }
      } ~
        pathPrefix("movie" / LongNumber) { id =>
          val movie = dbInstance.findById(id.toString)
          onSuccess(movie) {
            case Some(res) => complete(res)
            case None => complete(StatusCodes.NotFound)
          }

        }
    }
  }

}
