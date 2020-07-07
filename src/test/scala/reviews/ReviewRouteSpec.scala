package reviews

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import authentication.JWTGenerator
import models.{LoggedInUser, LoginRequest, ReviewCompDTO, ReviewExist}
import movies.{MovieRepository, MovieRoute, MovieService, MoviesMockDB}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import user.UserMockDB
import utils.MarshallFormatImplicits


class ReviewRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with MarshallFormatImplicits  {

  val reviewDB = ReviewMockDB
  val userDB = UserMockDB
  val movieDB = MoviesMockDB
  val movieReviewDB = MovieUserViewMockDB

  val jwtToken = new JWTGenerator

  val reviewDb = new ReviewsRepository
  val movieDb = new MovieRepository

  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)

  "The Review Route service" should {
    "return 200 and valid token after successful login" in {
      val username = "test1"
      val token =  jwtToken.getToken(username)
      val reviewData = ReviewCompDTO("test1", "0")
        Post("/reviewable", reviewData) ~> addHeader("Authorization", token) ~> movieRoute.isReviewable ~> check {
          status shouldEqual StatusCodes.OK
          val isReviewable = responseAs[ReviewExist]
          isReviewable.reviewExists shouldEqual "True"
        }
    }
  }
}
