package user

import models.User
import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.Future

class UserDTO(tag: Tag) extends Table[User](tag, "User") {
  def id = column[String]("ID", O.PrimaryKey)
  def email = column[String]("Email")
  def password = column[String]("Password")
  def displayName = column[String]("DisplayName")
  def * = (id, email, password, displayName) <> (User.tupled, User.unapply)
}

class UserRepository {

  private val users = TableQuery[UserDTO]

  private val db = Database.forConfig("movies")

  def findByUsername(email: String): Future[Option[User]] = {
    val searchId = users.filter(_.email === email)
    db.run(searchId.to[List].result.headOption)
  }

}
