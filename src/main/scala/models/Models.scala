package models

final case class Movie(id: String, title: String, year: String)
final case class Movies(movies: List[Movie])

final case class LoginRequest(username: String, password: String)
final case class User(id:String, name: String, password: String, displayName: String)
final case class LoggedInUser(id:String, name: String, jwtToken: String, displayName: String)
final case class ErrorInfo(code: Int, message: String)

