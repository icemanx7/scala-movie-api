import java.time.Instant

import models.{Movie, MovieRepository}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtHeader, JwtSprayJson}

import scala.util.{Failure, Success}
// for JSON serialization/deserialization following dependency is required:
// "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7"
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

import scala.concurrent.Future

final case class Movies(movies: List[Movie])
final case class LoginRequest(username: String, password: String)

object Main {

  val dbInstance = new MovieRepository
  private val tokenExpiryPeriodInDays = 1
  private val secretKey               = "super_secret_key"
  val algo = JwtAlgorithm.HS256
  val claim = JwtClaim(
         expiration = Some(Instant.now.plusSeconds(157784760).getEpochSecond),
         issuedAt = Some(Instant.now.getEpochSecond)
       )
  val token = JwtSprayJson.encode(claim, secretKey, algo)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  // formats for unmarshalling and marshalling
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val moviesFormat = jsonFormat1(Movies)
  implicit val loginRequest = jsonFormat2(LoginRequest)

  def main(args: Array[String]) {

    val route: Route =
      get {
        pathPrefix("movies" ) {
          val maybeItem:Future[List[Movie]] = dbInstance.getAll
          print(token)

           onSuccess(maybeItem) {
            case res => complete(res)
            case _ => complete(StatusCodes.NotFound)
          }

        }
      } ~
        post {
          entity(as[LoginRequest]) {
            case lr@LoginRequest("admin", "admin") =>
              respondWithHeader(RawHeader("Access-Token", token)) {
                complete(StatusCodes.OK)
              }
            case LoginRequest(_, _) => complete(StatusCodes.Unauthorized)
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }


}
