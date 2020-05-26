package user

import models.LoginRequest
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.StatusCodes
import utils.MarshallFormatImplicits

class UserLogin(userService: UserService) extends Directives with  MarshallFormatImplicits{

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

