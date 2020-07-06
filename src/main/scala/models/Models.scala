package models

final case class Movie(id: String, title: String, year: String)

final case class MovieDTO(id: String, title: String, year: String, isReviewable: Boolean)
final case class Movies(movies: List[Movie])

final case class LoginRequest(username: String, password: String)
final case class User(id:String, email: String, password: String, displayName: String)
final case class LoggedInUser(id:String, name: String, jwtToken: String, displayName: String)
final case class ErrorInfo(code: Int, message: String)

final case class MovieReview(username: String, rating: Double, review: String, reviewDate: String, movieID: String)

final case class Review(id: Option[String], review: String, reviewRating: Double, reviewDate: String)

//FOR THE DB Coms
final case class ReviewComp(movieReviewID: Option[String], movieID: String, reviewID: String, userID: String, username: String)

// UI <-> BE
final case class ReviewCompDTO(username: String, movieId: String)

final case class ReviewExist(reviewExists: String)

