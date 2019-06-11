package user

import authentication.AuthenticationDirectives._
import models.{LoginRequest, Movie, MovieRepository}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import spray.json.DefaultJsonProtocol._
import utils.MarshallFormatImplicits._

import scala.concurrent.Future

class UserLogin(dbInstance: MovieRepository) {

  // Add the userService That will check the credentials.
  def login: Route = post {
    entity(as[LoginRequest]) {
      case LoginRequest("admin", "admin") =>
        respondWithHeader(RawHeader("Access-Token", jwtToken.getToken)) {
          complete(StatusCodes.OK)
        }
      case LoginRequest(_, _) => complete(StatusCodes.Unauthorized)
    }
  }

}
