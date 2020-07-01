import models.User
import slick.jdbc.SQLiteProfile.api._
import user._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


class UserMockDB {
  val db = Database.forConfig("movies")
  private val users = TableQuery[UserDTO]
  val action: DBIO[Unit] = users.schema.create
  val future: Future[Unit] = db.run(action)
  val result = Await.result(future, 2.seconds)

  //    final case class User(id:String, email: String, password: String, displayName: String)

  def freshTestData = Seq(
    User("0", "test1", "test1", "icemanx7"),
    User("1", "test2", "test2", "s1mple|Hax")
  )

  val insert: DBIO[Option[Int]] = users ++= freshTestData
  val insertAction: Future[Option[Int]] = db.run(insert)
  val rowCount = Await.result(insertAction, 2.seconds)
}
