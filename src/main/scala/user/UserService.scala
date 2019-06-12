package user

import models.{LoggedInUser, LoginRequest}
import scala.concurrent.{ExecutionContext, Future}

class UserService(dbInstance: UserRepository)
                 (implicit executionContext: ExecutionContext) {

  def login(loginUser: LoginRequest): Future[Option[LoggedInUser]] = {
    dbInstance.findByUsername(loginUser.username)
      .filter(user => {
        user match  {
          case Some(realUser) => "1" == "1"
          case None => false
        }
      }
      )
      .map(user => user.map(realUser => LoggedInUser(realUser.id, realUser.name)))
  }

}
