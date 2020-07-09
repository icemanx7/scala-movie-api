package movies

import reviews.{MovieUserViewMockDB, ReviewMockDB, ReviewsRepository}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import authentication.JWTGenerator
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import user.UserMockDB
import utils.MarshallFormatImplicits


class MovieSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with MarshallFormatImplicits  {

  val movieDB = MoviesMockDB
  val reviewDB = ReviewMockDB
  val userDB = UserMockDB
  val movieReviewDB = MovieUserViewMockDB

  val jwtToken = new JWTGenerator

  val movieDb = new MovieRepository
  val reviewDb = new ReviewsRepository

  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)

  "The Movie Route service" should {
    "return 200" in {
      val username = "test2"
      val token =  jwtToken.getToken(username)
      Get("/movies") ~> addHeader("Authorization", token) ~> movieRoute.getMovieEntities ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }

}
