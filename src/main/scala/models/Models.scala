package models

final case class Movie(id: String, title: String, year: String)
final case class Movies(movies: List[Movie])
final case class LoginRequest(username: String, password: String)