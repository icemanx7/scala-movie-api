package reviews

import models.{Review, User}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object ReviewMockDB {

  private val db = Database.forConfig("movies")
  private val reviews = TableQuery[ReviewDTO]
  private val action: DBIO[Unit] = reviews.schema.create
  private lazy val future: Future[Unit] = db.run(action)
  private val result = Await.result(future, 2.seconds)


  def freshTestData = Seq(
    Review(Some("0"), "great movie", 7.5, "today"),
    Review(Some("1"), "Bad movie", 1.5, "yesterDay")
  )

  val insert: DBIO[Option[Int]] = reviews ++= freshTestData
  val insertAction: Future[Option[Int]] = db.run(insert)
  val rowCount = Await.result(insertAction, 2.seconds)

}
