package authentication

import java.time.Instant

import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}

class JWTGenerator {

  val secretKey = "super_secret_key"
  val algorithm = JwtAlgorithm.HS256
  val secondsToAdd = 1000

  def getToken(): String = {
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(secondsToAdd).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond)
    )
    JwtSprayJson.encode(claim, secretKey, algorithm)
  }


}
