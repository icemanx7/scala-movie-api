import models.{Movie, MovieRepository}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import scala.util.{Failure, Success}
// for JSON serialization/deserialization following dependency is required:
// "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7"
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

import scala.concurrent.Future

object Main {

  val dbInstance = new MovieRepository

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  final case class Movies(movies: List[Movie])

  // formats for unmarshalling and marshalling
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val moviesFormat = jsonFormat1(Movies)

  def main(args: Array[String]) {

    val route: Route =
      get {
        pathPrefix("movies" ) {
          val maybeItem:Future[List[Movie]] = dbInstance.getAll

           onSuccess(maybeItem) {
            case res => complete(res)
            case _ => complete(StatusCodes.NotFound)
          }

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
