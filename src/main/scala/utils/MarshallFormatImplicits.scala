package utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{ErrorInfo, InsertResp, JWTContent, LoggedInUser, LoginRequest, Movie, MovieDTO, MovieReview, Movies, Review, ReviewComp, ReviewCompDTO, ReviewExist}
import spray.json.DefaultJsonProtocol

trait MarshallFormatImplicits extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val movieDto = jsonFormat4(MovieDTO)
  implicit val moviesFormat = jsonFormat1(Movies)
  implicit val loginRequest = jsonFormat2(LoginRequest)
  implicit val loginUserRequest = jsonFormat4(LoggedInUser)
  implicit val errorInfo = jsonFormat2(ErrorInfo)
  implicit val movieReview = jsonFormat5(MovieReview)
  implicit val review = jsonFormat4(Review)
  implicit val movieReviewComp = jsonFormat2(ReviewCompDTO)
  implicit val reviewExists = jsonFormat1(ReviewExist)
  implicit val jwtContent = jsonFormat1(JWTContent)
  implicit val insertresp = jsonFormat2(InsertResp)
}
