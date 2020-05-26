package user

import authentication.AuthenticationDirectives.jwtToken
import models.{LoggedInUser, LoginRequest}

import scala.concurrent.{ExecutionContext, Future}
import utils.MonadTransformers._

class UserService(dbInstance: UserRepository)
                 (implicit executionContext: ExecutionContext) {

  def login(loginUser: LoginRequest): Future[Option[LoggedInUser]] = {
    dbInstance.findByUsername(loginUser.username)
      .filterT(user => {
        println(user)
        user.password == loginUser.password})
      .mapT(user => LoggedInUser(user.id, user.name, jwtToken.getToken, user.displayName))
  }

}
