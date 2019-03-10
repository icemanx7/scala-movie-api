import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.syntax._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api._
import io.circe.syntax._

import scala.concurrent.Await
import scala.concurrent.duration._
import cats.effect._
import io.circe._
import io.circe.literal._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import models.{Movie, MovieRepository}
import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.middleware.CORS



class Main {

}



object Main extends IOApp {

  val dbInstance = new MovieRepository

  implicit val HelloEncoder: Encoder[Movie] =
    Encoder.instance { movie: Movie =>
      json"""
             {
            "id": ${movie.id},
            "title": ${movie.title},
            "year": ${movie.year}
             }"""
    }

  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "movies" =>
      val dd = dbInstance.getAll
      val result = Await.result(dd, 1.seconds)
      Ok(result.asJson)

    case GET -> Root / "movies" / id =>
      val dd = dbInstance.findById(id)
      val result = Await.result(dd, 1.seconds)
      Ok(result.asJson)


  }.orNotFound

  val corsService = CORS(helloWorldService)

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(corsService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}