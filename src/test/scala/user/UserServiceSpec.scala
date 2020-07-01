package user

import models.LoginRequest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceSpec extends AnyWordSpec with Matchers {

  val userDB = new UserMockDB
  val userDb = new UserRepository
  val userService = new UserService(userDb)

  "The User service" should {
    "return the supplied login user details" in {
      val loginUser = LoginRequest("test1", "test1")
      val loggedInUser = userService.login(loginUser)
      val result = Await.result(loggedInUser, 2.seconds)
      result match {
        case Some(currentUser) => {
          currentUser.name shouldEqual loginUser.username
        }
      }
    }
  }
}
