
import org.specs2.mutable.Specification
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import akka.http.scaladsl.server._
import Directives._
import models.LoginRequest
import movies.{MovieRepository, MovieRoute, MovieService}
import reviews.ReviewsRepository
import user.{UserLogin, UserRepository, UserService}
import utils.MarshallFormatImplicits

class FullSpecs2TestKitExampleSpec extends Specification with Specs2RouteTest with MarshallFormatImplicits {

  val fakeUser = LoginRequest("test1", "test1")

  val movieDb = new MovieRepository
  val userDb = new UserRepository
  val reviewDb = new ReviewsRepository

  val userService = new UserService(userDb)
  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)
  val userRoute = new UserLogin(userService)
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

    "return a greeting for GET requests to the root path" in {
      // tests:
      Post("/login", fakeUser) ~> userRoute.login ~> check {
        status shouldEqual  StatusCodes.OK
        responseAs[String] shouldEqual "Captain on the bridge!"
      }
    }

    "return a 'PONG!' response for GET requests to /ping" in {
      // tests:
      Get("/ping") ~> smallRoute ~> check {
        responseAs[String] shouldEqual "PONG!"
      }
    }

    "leave GET requests to other paths unhandled" in {
      // tests:
      Get("/kermit") ~> smallRoute ~> check {
        handled should beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      // tests:
      Put() ~> Route.seal(smallRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}

