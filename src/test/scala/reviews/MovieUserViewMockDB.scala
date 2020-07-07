package reviews

import models.{Review, ReviewComp, User}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object MovieUserViewMockDB {

  private val db = Database.forConfig("movies")
  private val movieReviewUserDTO = TableQuery[MovieUserReviewDTO]
  private val action: DBIO[Unit] = movieReviewUserDTO.schema.create
  private lazy val future: Future[Unit] = db.run(action)
  private val result = Await.result(future, 2.seconds)


  def freshTestData = Seq(
    ReviewComp(Some("0"),"0", "0", "0", "test1"),
  )

  val insert: DBIO[Option[Int]] = movieReviewUserDTO ++= freshTestData
  val insertAction: Future[Option[Int]] = db.run(insert)
  val rowCount = Await.result(insertAction, 2.seconds)

}