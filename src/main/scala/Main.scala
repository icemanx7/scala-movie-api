import models.MovieRepository
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn
import movies.MovieRoute
import user.UserLogin

object Main {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val dbInstance = new MovieRepository
  val movieRoute = new MovieRoute(dbInstance)
  val userRoute = new UserLogin(dbInstance)

  def main(args: Array[String]) {

    //TODO: Move this to the route folder
    val route: Route = movieRoute.getListMovies ~ userRoute.login

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}
