package models

final case class Movie(id: String, title: String, year: String)

final case class MovieDTO(id: String, title: String, year: String, isReviewable: Boolean)
final case class Movies(movies: List[MovieDTO])

final case class LoginRequest(username: String, password: String)
final case class User(id:String, email: String, password: String, displayName: String)
final case class LoggedInUser(id:String, name: String, jwtToken: String, displayName: String)
final case class ErrorInfo(code: Int, message: String)

final case class InsertResp(code: Int, message: String)

final case class MovieReview(username: String, rating: Double, review: String, reviewDate: String, movieID: String)

final case class Review(id: Option[Int], review: String, reviewRating: Double, reviewDate: String)

//FOR THE DB Coms
final case class ReviewComp(movieReviewID: Option[Int], movieID: String, reviewID: Int, userID: String)

// UI <-> BE
final case class ReviewCompDTO(username: String, movieId: String)

final case class ReviewExist(reviewExists: String)

final case class JWTContent(username: String)


final case class Rating ( Source: String,
                          Value: String
                        )

final case class DetailedMovie (
                                 Title: String,
                                 Rated: String,
                                 Released: String,
                                 Runtime: String,
                                 Genre: String,
                                 Director: String,
                                 Actors: String,
                                 Plot: String,
                                 Language: String,
                                 Country: String,
                                 Awards: String,
                                 Poster: String,
                                 Ratings: List[Rating],
                                 Metascore: String,
                                 imdbRating: String,
                                 imdbVotes: String,
                                 imdbID: String,
                                 Type: String,
                                 DVD: String,
                                 BoxOffice: String,
                                 Website: String,
                                 Response: String,
                               )


