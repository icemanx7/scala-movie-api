package reviews

import models.{Review, ReviewComp, ReviewCompDTO}
import slick.jdbc.SQLiteProfile.api._
import user.{UserDTO, UserRepository}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
  def id = column[String]("ReviewID", O.PrimaryKey)
  def review = column[String]("Review")
  def reviewDate = column[String]("ReviewDate")
  def reviewRating = column[Double]("ReviewRating")
  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
}

class MovieUserReviewDTO(tag: Tag) extends Table[ReviewComp](tag, "Reviews") {
  def movieReviewID = column[String]("MovieReviewID", O.PrimaryKey)
  def movieID = column[String]("MovieID")
  def reviewID = column[String]("ReviewID")
  def userID = column[String]("UserID")
  def * = (movieReviewID.?, movieID, reviewID, userID) <> (ReviewComp.tupled, ReviewComp.unapply)
}

class ReviewsRepository() {

  val reviewTable = TableQuery[ReviewDTO]
  val movieReviewTable = TableQuery[MovieUserReviewDTO]
  val userTable = TableQuery[UserDTO]

  val db = Database.forConfig("movies")

  //Opp to learn about queues and transactions YESSS
  def insertReview(review: Review): Future[Any] = {
    val insertAction = reviewTable returning reviewTable.map(_.id) += review
    val res = db.run(insertAction)
    println("RESULT: ", Await.result(res, 4.seconds))
    res
  }

  def doesReviewExist(reviewCom: ReviewCompDTO) = {
    val userId = userTable.filter(user => user.email === reviewCom.username)
    val doesEx  = for {
      userID <- userTable if userID.email == reviewCom.username
      movieID <- movieReviewTable if movieID.userID == userId && movieID.movieID == reviewCom.movieId
    } yield ( userID, movieID )
    val res = db.run(doesEx.exists.result)
    println("RESULT: ", Await.result(res, 4.seconds))
    res
  }

}
