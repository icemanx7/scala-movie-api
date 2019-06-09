import java.time.Instant
import models.{Movie, MovieRepository}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.{Directive1, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

import scala.concurrent.Future

final case class Movies(movies: List[Movie])
final case class LoginRequest(username: String, password: String)


object Main {

  val dbInstance = new MovieRepository

  private val tokenExpiryPeriodInDays = 1
  private val secretKey = "super_secret_key"
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

  private def authenticated: Directive1[String] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwt) if JwtSprayJson.isValid(jwt) =>
        complete(StatusCodes.Unauthorized -> "Token expired.")

      case Some(jwt) if JwtSprayJson.isValid(jwt, secretKey, Seq(JwtAlgorithm.HS256)) =>
        provide(JwtSprayJson.decode(jwt).toString)

      case _ => complete(StatusCodes.Unauthorized)
    }


  private def getListMovies: Route = get {
    authenticated { claims  =>
      pathPrefix("movies" ) {
        val maybeItem:Future[List[Movie]] = dbInstance.getAll
        print(token)
        print(claims)

        onSuccess(maybeItem) {
          case res => complete(res)
          case _ => complete(StatusCodes.NotFound)
        }
      }
    }
  }

  private def login: Route = post {
    entity(as[LoginRequest]) {
      case LoginRequest("admin", "admin") =>
        respondWithHeader(RawHeader("Access-Token", token)) {
          complete(StatusCodes.OK)
        }
      case LoginRequest(_, _) => complete(StatusCodes.Unauthorized)
    }
  }

  private def getSingleMovie: Route = ???

  private def findMovies: Route = ???


  def main(args: Array[String]) {

    val route: Route = getListMovies ~ login

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }


}
