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
import models.MovieRepository



class Main {

}

object Main extends IOApp {
  //  implicit val HelloEncoder: Encoder[List[(String, String, String)]] =
  //    Encoder.instance { hello: List[(String, String, String)] =>
  //      json"""{"hello": ${hello.head._3}}"""
  //    }

  //  implicit def tweetEncoder: EntityEncoder[IO, Json] = ???
  val dbInstance = new MovieRepository

  def hello(name: String): Json =
    json"""{"hello": $name}"""
  // hello: (name: String)io.circe.Json

  val greeting = hello("world")

  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      print("asdasdasd")
      val dd = dbInstance.getAll
      val result = Await.result(dd, 10.seconds)
      Ok(result.asJson)
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(helloWorldService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)


    //      val proc = db.run(movies.to[List].result).map(_.map {
    //    case (name) =>
    //      println(name)
    //      name
    //  })
    //  Await.result(proc, 10.seconds)

  //  def main(args: Array[String]): Unit = {
  //    println("Running")
  //
  //    val movies = TableQuery[Movies]
  //
  //    val db = Database.forConfig("movies")
  //    try {
  //      val setup = DBIO.seq(
  //        (movies.schema).createIfNotExists
  //      )
  //      val setupFuture = db.run(setup)
  //      println("Done")
  //      val proc =    setupFuture.flatMap { _ =>
  //        println("Coffees:")
  //        db.run(movies.result).map(_.map {
  //          case (name) =>
  //            name
  //        })
  //      }
  //
  //      val result = Await.result(proc, 10.seconds)
  //      print(result)
  //    } finally db.close
  //  }
}