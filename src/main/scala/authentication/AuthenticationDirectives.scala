package authentication


import akka.http.scaladsl.server.{Directive1, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}

object AuthenticationDirectives {

  val jwtToken = new JWTGenerator

  def authenticated: Directive1[String] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwt) if JwtSprayJson.isValid(jwt) =>
        complete(StatusCodes.Unauthorized -> "Token expired.")

      case Some(jwt) if JwtSprayJson.isValid(jwt, jwtToken.secretKey, Seq(JwtAlgorithm.HS256)) =>
        provide(JwtSprayJson.decode(jwt).toString)

      case _ => complete(StatusCodes.Unauthorized)
    }
}
