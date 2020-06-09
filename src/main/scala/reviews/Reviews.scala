package reviews

import models.Review
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
  def id = column[String]("ReviewID", O.PrimaryKey)
  def review = column[String]("Review")
  def reviewDate = column[String]("ReviewDate")
  def reviewRating = column[Double]("ReviewRating")
  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
}

//class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
//  def id = column[String]("ReviewID", O.PrimaryKey)
//  def review = column[String]("Review")
//  def reviewDate = column[String]("ReviewDate")
//  def reviewRating = column[Double]("ReviewRating")
//  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
//}

class ReviewsRepository {

  val reviewTable = TableQuery[ReviewDTO]
  val db = Database.forConfig("movies")

  //Opp to learn about queues and transactions YESSS
  def insertReview(review: Review): Future[Any] = {
    val insertAction = reviewTable returning reviewTable.map(_.id) += review
    val res = db.run(insertAction)
    println("RESULT: ", Await.result(res, 4.seconds))
    res

  }

}
