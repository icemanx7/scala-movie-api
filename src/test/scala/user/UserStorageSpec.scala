package user

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.concurrent.duration._
import scala.concurrent.Await

class UserStorageSpec extends AnyWordSpec with Matchers{

  val userDBMockData = UserMockDB
  val userDb = new UserRepository

  "The User service" should {
    "return the user from the User Table in the DB" in {
      val username = "test1"
      val userFuture = userDb.findByUsername(username)
      val result = Await.result(userFuture, 2.seconds)
      result match {
        case Some(returnedUser) => {
          returnedUser.email shouldEqual  username
        }
        case _ => fail("user was not found")
      }

    }
  }

}
