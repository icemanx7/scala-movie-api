package user

import authentication.AuthenticationDirectives._
import models.LoginRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import utils.MarshallFormatImplicits._

class UserLogin(userService: UserService) {

  def login: Route = post {
    pathPrefix("login" ) {
      entity(as[LoginRequest]) { user =>
        val isIndatabase = userService.login(user)
        onSuccess(isIndatabase) {
          case Some(realUser) =>
              complete(realUser)
          case None => complete(StatusCodes.Unauthorized)
        }
      }
    }
  }
}

