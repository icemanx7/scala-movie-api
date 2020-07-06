package reviews

import models.{Review, ReviewComp, ReviewCompDTO, ReviewExist}
import slick.jdbc.SQLiteProfile.api._
import user.{UserDTO, UserRepository}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
  def id = column[String]("ReviewID", O.PrimaryKey)
  def review = column[String]("Review")
  def reviewDate = column[String]("ReviewDate")
  def reviewRating = column[Double]("ReviewRating")
  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
}

class MovieUserReviewDTO(tag: Tag) extends Table[ReviewComp](tag, "MovieReview") {
  def movieReviewID = column[String]("MovieReviewID", O.PrimaryKey)
  def movieID = column[String]("MovieID")
  def reviewID = column[String]("ReviewID")
  def userID = column[String]("UserID")
  def username = column[String]("Username")
  def * = (movieReviewID.?, movieID, reviewID, userID, username) <> (ReviewComp.tupled, ReviewComp.unapply)
}

class ReviewsRepository() (implicit executionContext: ExecutionContext) {

  val reviewTable = TableQuery[ReviewDTO]
  val movieReviewTable = TableQuery[MovieUserReviewDTO]
  val userTable = TableQuery[UserDTO]

  val db = Database.forConfig("movies")

  //Opp to learn about queues and transactions YESSS
  def insertReview(review: Review): Future[Any] = {
    val insertAction = reviewTable returning reviewTable.map(_.id) += review
    val res = db.run(insertAction)
    res
  }

  def doesReviewExist(reviewCom: ReviewCompDTO): Future[ReviewExist] = {
    val userId = userTable.filter(user => user.email === reviewCom.username)
    val doesEx  = for {
      movieID <- movieReviewTable if  movieID.movieID === reviewCom.movieId
      movieIDIn <- movieReviewTable if  movieIDIn.username === reviewCom.username
    } yield ( movieID, movieIDIn)
    val isThereReview = db.run(doesEx.exists.result)
    isThereReview.map(isReview => {
      if(isReview) {
        ReviewExist("True")
      }
      else {
        ReviewExist("False")
      }
    })
  }

}
