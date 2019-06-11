package utils

import models.{LoginRequest, Movie, Movies}
import spray.json.DefaultJsonProtocol._

object MarshallFormatImplicits {
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val moviesFormat = jsonFormat1(Movies)
  implicit val loginRequest = jsonFormat2(LoginRequest)
}
