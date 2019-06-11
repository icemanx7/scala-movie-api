package authentication

import java.time.Instant

import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}

class JWTGenerator {

  val secretKey = "super_secret_key"
  val algorithm = JwtAlgorithm.HS256
  val claim = JwtClaim(
    expiration = Some(Instant.now.plusSeconds(1000).getEpochSecond),
    issuedAt = Some(Instant.now.getEpochSecond)
  )

  def getToken(): String = {
    JwtSprayJson.encode(claim, secretKey, algorithm)
  }


}
