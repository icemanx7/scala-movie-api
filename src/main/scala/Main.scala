import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import movies.{MovieRepository, MovieRoute, MovieService}
import reviews.ReviewsRepository
import user.{UserLogin, UserRepository, UserService}

object Main {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val movieDb = new MovieRepository
  val userDb = new UserRepository
  val reviewDb = new ReviewsRepository

  val userService = new UserService(userDb)
  val movieService = new MovieService(movieDb, reviewDb)
  val movieRoute = new MovieRoute(movieService)
  val userRoute = new UserLogin(userService)

  def main(args: Array[String]) {

    //TODO: Move this to the route folder
    val route: Route = userRoute.login ~ movieRoute.getMovieEntities ~ movieRoute.submitMovieReview

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}
