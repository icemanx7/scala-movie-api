package movies

import reviews.ReviewsRepository
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import authentication.JWTGenerator
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.MarshallFormatImplicits


class MovieSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with MarshallFormatImplicits  {

  val movieDB = MoviesMockDB
  val jwtToken = new JWTGenerator

  val movieDb = new MovieRepository
  val reviewDb = new ReviewsRepository

  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)

  "The Movie Route service" should {
    "return 200" in {
      val username = "test1"
      val token =  jwtToken.getToken(username)
      Get("/movies") ~> addHeader("Authorization", token) ~> movieRoute.getMovieEntities ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }

}
