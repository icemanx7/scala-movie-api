package user

import models.User
import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.Future

class UserDTO(tag: Tag) extends Table[User](tag, "User") {
  def id = column[String]("ID", O.PrimaryKey)
  def name = column[String]("Name")
  def password = column[String]("Password")
  def * = (id, name, password) <> (User.tupled, User.unapply)
}

class UserRepository {

  private val users = TableQuery[UserDTO]

  private val db = Database.forConfig("movies")

  def findByUsername(username: String): Future[Option[User]] = {
    val searchId = users.filter(_.name === username)
    db.run(searchId.to[List].result.headOption)
  }

}
