package authentication

import java.time.Instant

import models.JWTContent
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import utils.MarshallFormatImplicits
import spray.json.DefaultJsonProtocol
import spray.json._

class JWTGenerator extends MarshallFormatImplicits{

  val secretKey = "super_secret_key"
  val algorithm = JwtAlgorithm.HS256
  val secondsToAdd = 1000

  def getToken(username: String): String = {
    val tokenContent = JWTContent(username).toJson.toString()
    val claim = JwtClaim(
      content = tokenContent,
      expiration = Some(Instant.now.plusSeconds(secondsToAdd).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    JwtSprayJson.encode(claim, secretKey, algorithm)
  }


}
