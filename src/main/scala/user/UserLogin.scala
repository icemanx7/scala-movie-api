package user

import authentication.AuthenticationDirectives._
import models.LoginRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import movies.MovieRepository
import utils.MarshallFormatImplicits._

class UserLogin(userService: UserService) {

  // Add the userService That will check the credentials.
  def login: Route = post {
    entity(as[LoginRequest]) { user =>
      val isIndb = userService.login(user)
      onSuccess(isIndb) {
        case Some(realUser) =>
          respondWithHeader(RawHeader("Access-Token", jwtToken.getToken)) {
            complete(realUser)
          }
        case None => complete(StatusCodes.Unauthorized)
      }
    }

  }
}

