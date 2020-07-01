import org.scalatest._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import authentication.JWTGenerator
import models.{LoggedInUser, LoginRequest}
import movies.{MovieRepository, MovieRoute, MovieService}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import reviews.ReviewsRepository
import user._
import utils.MarshallFormatImplicits
import slick.jdbc.SQLiteProfile.api._

class LoginRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with MarshallFormatImplicits  {

  val userDB = new UserMockDB
  val jwtToken = new JWTGenerator

  val movieDb = new MovieRepository
  val userDb = new UserRepository
  val reviewDb = new ReviewsRepository

  val userService = new UserService(userDb)
  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)
  val userRoute = new UserLogin(userService)

  val loginUser = LoginRequest("test1", "test1")
  val wrongLoginUser = LoginRequest("test", "test1")

  val smallRoute =
    get {
      concat(
        pathSingleSlash {
          complete {
            "Captain on the bridge!"
          }
        },
        path("ping") {
          complete("PONG!")
        }
      )
    }

  "The service" should {
    "return 200 and valid token after successful login" in {
      Post("/login", loginUser) ~> userRoute.login ~> check {
        status shouldEqual StatusCodes.OK
        val resp = responseAs[LoggedInUser]
        val jwt = JwtSprayJson.isValid(resp.jwtToken, jwtToken.secretKey, Seq(JwtAlgorithm.HS256))
        jwt shouldEqual true
      }
    }

    "return 401 after unsuccessful login" in {
      // tests:
      Post("/login", wrongLoginUser) ~> userRoute.login ~> check {
        status shouldEqual StatusCodes.Unauthorized
      }
    }
  }
}