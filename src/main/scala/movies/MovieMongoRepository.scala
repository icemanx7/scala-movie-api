package movies

import models.DetailedMovie
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observable}
import spray.json._
import utils.MarshallFormatImplicits

import scala.concurrent.Future

class MovieMongoRepository extends  MarshallFormatImplicits{


  def getAllMovieMetaData(): Future[Seq[DetailedMovie]] = {
    val mongoClient = MongoClient()
    val database: MongoDatabase = mongoClient.getDatabase("movies")
    val coll: MongoCollection[Document] = database.getCollection("singleMovies")
    coll.find().map(listOfMovies => {
     listOfMovies.toJson.parseJson.convertTo[DetailedMovie]
    }).toFuture()
  }

}
