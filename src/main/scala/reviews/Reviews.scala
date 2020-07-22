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
  def movieReviewID = column[Int]("MovieReviewID", O.PrimaryKey, O.AutoInc)
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

  //Opp to learn about queues and transactions YESSS or split it into functions. Map it out
  def insertReview(review: Review, username: String, movieID: String): Future[Int] = {
    val isThereReview = doesReviewExist(username, movieID)

    //Check if review exist? Can't have duplicate reviews bra
    val insertAction = reviewTable returning reviewTable.map(_.id) += review
    val user = userTable.filter(_.email === username)
    val userFuture = db.run(user.to[List].result.headOption)
    val res = db.run(insertAction)
    val reviewCombo = for {
      currentUser <- userFuture.mapT(getUserId)
      reviewId <- res
      isThereReviewNow <- isThereReview
    } yield (currentUser, reviewId, isThereReviewNow)
    val currentReviews = db.run(movieReviewTable.to[List].result)

    val whatIsThis = reviewCombo.flatMap{ case (userId, reviewId, doesCurrentReviewExist) =>{
      userId match  {
        case Some(userItem)  => {
          if (!doesCurrentReviewExist) {
            val insertReviewComp = ReviewComp(None, movieID,reviewId, userItem)
            val reviewCompInsertAction = movieReviewTable += insertReviewComp
            db.run(reviewCompInsertAction)
          }
          else {
            Future { 0 }
          }
        }
      }
    }}
    whatIsThis
  }

  private def getUserID(username: String): Future[Option[User]] = {
    val user = userTable.filter(_.email === username)
    db.run(user.to[List].result.headOption)
  }

  private def doesReviewExist(username: String, movieID: String): Future[Boolean] = {
    val userid = getUserID(username)
    userid.flatMap(userId => {
      userId match {
        case Some(idNum) => {
          val currentReviews = movieReviewTable.filter(movieReview => movieReview.userID === idNum.id && movieReview.movieID === movieID)
          db.run(currentReviews.exists.result)
        }
      }
    })
  }

  // TODO: Make a parent function of the class like a trait vibe
  private def resolveFuture[A](fut: Future[A]): A = {
    Await.result(fut, 2.seconds)
  }


  def getUserId(user: User): String = {
    user.id
  }

  def getAllEntries() = {
    db.run(movieReviewTable.to[List].result)
  }


}
