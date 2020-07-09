package user

import models.User
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object UserMockDB {

  private val db = Database.forConfig("movies")
  private val users = TableQuery[UserDTO]
  private val action: DBIO[Unit] = users.schema.create
  private lazy val future: Future[Unit] = db.run(action)
  private val result = Await.result(future, 2.seconds)

  def freshTestData = Seq(
    User("0", "test1", "test1", "icemanx7"),
    User("1", "test2", "test2", "s1mple|Hax"),
    User("3", "abab", "baba", "ASD")
  )

  val insert: DBIO[Option[Int]] = users ++= freshTestData
  val insertAction: Future[Option[Int]] = db.run(insert)
  val rowCount = Await.result(insertAction, 2.seconds)
}
