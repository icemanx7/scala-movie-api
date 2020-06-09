package utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{ErrorInfo, LoggedInUser, LoginRequest, Movie, MovieReview, Movies, Review, ReviewComp}
import spray.json.DefaultJsonProtocol

trait MarshallFormatImplicits extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val moviesFormat = jsonFormat1(Movies)
  implicit val loginRequest = jsonFormat2(LoginRequest)
  implicit val loginUserRequest = jsonFormat4(LoggedInUser)
  implicit val errorInfo = jsonFormat2(ErrorInfo)
  implicit val movieReview = jsonFormat5(MovieReview)
  implicit val review = jsonFormat4(Review)
}
