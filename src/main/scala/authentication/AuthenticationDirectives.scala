package authentication

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.model.StatusCodes
import models.ErrorInfo
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import akka.http.scaladsl.server.Directives
import utils.MarshallFormatImplicits


object AuthenticationDirectives extends Directives with  MarshallFormatImplicits {

  val jwtToken = new JWTGenerator

  def authenticated: Directive1[String] =
    optionalHeaderValueByName("Authorization").flatMap {

      case Some(jwt) if JwtSprayJson.isValid(jwt, jwtToken.secretKey, Seq(JwtAlgorithm.HS256)) =>
        provide(JwtSprayJson.decode(jwt).toString)

      case Some(jwt) if JwtSprayJson.isValid(jwt) =>
        complete(StatusCodes.Unauthorized -> "Token expired.")

      case _ =>
        complete(401 -> ErrorInfo(401, "AA"))
    }
}
