package movies

import reviews.{MovieUserViewMockDB, ReviewMockDB, ReviewsRepository}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import authentication.JWTGenerator
import models.{MovieReview, Movies}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import user.UserMockDB
import utils.MarshallFormatImplicits
import scala.concurrent.duration._
import scala.concurrent.Await


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
    "return 200 when requesting movie entities" in {
      val username = "test2"
      val token =  jwtToken.getToken(username)
      Get("/movies") ~> addHeader("Authorization", token) ~> movieRoute.getMovieEntities ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "return 200 when inserting a full movie review" in {
      val username = "test1"
      val movieReviewItem = MovieReview(username, 8, "Very good testinng", "YESTERDAY","0")
      val token =  jwtToken.getToken(username)
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "fail when attempting multiple review inserts" in {
      val username = "test1"
      val numberOfMovies = 6
      val movieReviewItem = MovieReview(username, 0, "Very good testinng", "YESTERDAY","0")
      val token =  jwtToken.getToken(username)
      Get("/movies") ~> addHeader("Authorization", token) ~> movieRoute.getMovieEntities ~> check {
        status shouldEqual StatusCodes.OK
        val movies = responseAs[Movies]
        assert(numberOfMovies == movies.movies.length)
      }
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
      Post("/submitreview", movieReviewItem) ~> addHeader("Authorization", token) ~> movieRoute.submitMovieReview ~> check {
        status shouldEqual StatusCodes.OK
      }
      Get("/movies") ~> addHeader("Authorization", token) ~> movieRoute.getMovieEntities ~> check {
        status shouldEqual StatusCodes.OK
        val movies = responseAs[Movies]
        val dd = Await.result(reviewDb.getAllEntries(), 2.seconds)
        assert(numberOfMovies == movies.movies.length)
      }
    }
  }

}
