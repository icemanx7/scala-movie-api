package movies

import models.{Movie, Review}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Future


class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
  def id = column[String]("ReviewID", O.PrimaryKey)
  def review = column[String]("Review")
  def reviewDate = column[String]("ReviewDate")
  def reviewRating = column[Double]("ReviewRating")
  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
}

class ReviewsRepository {

  val reviewTable = TableQuery[ReviewDTO]
  val db = Database.forConfig("movies")

  def insertReview(review: Review): Future[Unit] = {
    val insertAction = DBIO.seq(
      reviewTable += review
    )
    db.run(insertAction)
  }

}
