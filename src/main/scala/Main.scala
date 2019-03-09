import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.syntax._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class Movies(tag: Tag) extends Table[(String, String, String)](tag, "MovieTitle") {
  def id = column[String]("ID", O.PrimaryKey)
  def title = column[String]("Name")
  def year = column[String]("Year")
  def * = (id, title, year)
}

class Main {

}

object Main {

  //  val helloWorldService = HttpRoutes.of[IO] {
  //    case GET -> Root / "hello" / name =>
  //      Ok(s"Hello, $name.")
  //  }.orNotFound
  //
  //  def run(args: List[String]): IO[ExitCode] =
  //    BlazeServerBuilder[IO]
  //      .bindHttp(8080, "localhost")
  //      .withHttpApp(helloWorldService)
  //      .serve
  //      .compile
  //      .drain
  //      .as(ExitCode.Success)

  def main(args: Array[String]): Unit = {
    println("Running")

    val movies = TableQuery[Movies]

    val db = Database.forConfig("movies")
    try {
      val setup = DBIO.seq(
        (movies.schema).createIfNotExists
      )
      val setupFuture = db.run(setup)
      println("Done")
      val proc =    setupFuture.flatMap { _ =>
        println("Coffees:")
        db.run(movies.result).map(_.foreach {
          case (name) =>
            println("  " + name)
        })
      }

      val result = Await.result(proc, 10.seconds)
    } finally db.close
  }
}