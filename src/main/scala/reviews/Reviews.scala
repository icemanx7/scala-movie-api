package reviews

import models.{Review, ReviewComp, ReviewCompDTO, ReviewExist, User}
import slick.jdbc.SQLiteProfile.api._
import user.{UserDTO, UserRepository}
import utils.MonadTransformers._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import cats.data.OptionT
import cats.implicits._

class ReviewDTO(tag: Tag) extends Table[Review](tag, "Reviews") {
  def id = column[Int]("ReviewID", O.PrimaryKey, O.AutoInc)
  def review = column[String]("Review")
  def reviewDate = column[String]("ReviewDate")
  def reviewRating = column[Double]("ReviewRating")
  def * = (id.?, review, reviewRating, reviewDate) <> (Review.tupled, Review.unapply)
}

class MovieUserReviewDTO(tag: Tag) extends Table[ReviewComp](tag, "MovieReview") {
  def movieReviewID = column[String]("MovieReviewID", O.PrimaryKey)
  def movieID = column[String]("MovieID")
  def reviewID = column[Int]("ReviewID")
  def userID = column[String]("UserID")
  def * = (movieReviewID.?, movieID, reviewID, userID) <> (ReviewComp.tupled, ReviewComp.unapply)
}

class ReviewsRepository() (implicit executionContext: ExecutionContext) {

  val reviewTable = TableQuery[ReviewDTO]
  val movieReviewTable = TableQuery[MovieUserReviewDTO]
  val userTable = TableQuery[UserDTO]

  val db = Database.forConfig("movies")

  //Opp to learn about queues and transactions YESSS
  def insertReview(review: Review, username: String, movieID: String): Future[Int] = {
    println("IS RUNNING")
    println("USER NAME: " + username)
    println("MOVIE ID: " + movieID)
    val insertAction = reviewTable returning reviewTable.map(_.id) += review
    val user = userTable.filter(_.email === username)
    val userFuture = db.run(user.to[List].result.headOption)
    val res = db.run(insertAction)
    val dd = for {
      currentUser <- userFuture.mapT(getUserId)
      reviewId <- res
    } yield (currentUser, reviewId)

    val whatIsThis = dd.flatMap{ case (userId, reviewId) =>{
      println("In flaptma")
      userId match  {
        case Some(userItem) => {
          println("MovieID: " + movieID)
          println("ReviewID: " + reviewId)
          println("UserID: " + userItem)
          val insertReviewComp = ReviewComp(None, movieID,reviewId, userItem)
          val reviewCompInsertAction = movieReviewTable += insertReviewComp
          db.run(reviewCompInsertAction)
        }
      }
    }}
    whatIsThis
  }

  def getUserId(user: User): String = {
    user.id
  }


}
