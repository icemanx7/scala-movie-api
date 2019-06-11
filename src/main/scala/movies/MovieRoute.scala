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

  private def getSingleMovie: Route = ???

  private def findMovies: Route = ???

  def getListMovies: Route = get {

    authenticated { claims  =>
      pathPrefix("movies" ) {
        val maybeItem:Future[List[Movie]] = dbInstance.getAll
        print(claims)

        onSuccess(maybeItem) {
          case res => complete(res)
          case _ => complete(StatusCodes.NotFound)
        }
      }
    }
  }

}
