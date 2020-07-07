package auth

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.{JWTContent, LoggedInUser}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import utils.MarshallFormatImplicits
import authentication.AuthenticationDirectives.jwtToken
import spray.json._
import DefaultJsonProtocol._

import scala.util.{Failure, Success}


class JwtGenSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with MarshallFormatImplicits  {

  "The token generator" should {
    "encode a valid JWT token" in {
      val username = "test1"
      val token = jwtToken.getToken(username)
      val isValidToken = JwtSprayJson.isValid(token, jwtToken.secretKey, Seq(JwtAlgorithm.HS256))
      isValidToken shouldEqual true
    }
    "encode and return the correct data" in {
      val username = "test1"
      val token = jwtToken.getToken(username)
      val decodedToken = JwtSprayJson.decode(token, jwtToken.secretKey,Seq(JwtAlgorithm.HS256))
      decodedToken match {
        case Success(claim) => {
          val user = claim.content.parseJson
          val userObj  = user.convertTo[JWTContent]
          userObj.username shouldEqual "test1"
        }
        case Failure(exception) => {
          fail(exception.getMessage)
        }
      }
    }
  }
}
