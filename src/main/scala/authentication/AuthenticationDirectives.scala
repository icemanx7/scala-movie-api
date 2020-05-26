package authentication


import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import models.ErrorInfo
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import utils.MarshallFormatImplicits

final case class Item(name: String, id: Long)
final case class Order(items: List[Item])

//trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
//  implicit val itemFormat = jsonFormat2(Item)
//  implicit val orderFormat = jsonFormat1(Order) // contains List[Item]
//}

object AuthenticationDirectives extends Directives with  MarshallFormatImplicits {

  val jwtToken = new JWTGenerator

  def authenticated: Directive1[String] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwt) if JwtSprayJson.isValid(jwt) =>
        complete(StatusCodes.Unauthorized -> "Token expired.")

      case Some(jwt) if JwtSprayJson.isValid(jwt, jwtToken.secretKey, Seq(JwtAlgorithm.HS256)) =>
        provide(JwtSprayJson.decode(jwt).toString)

      case _ =>
        complete(401 -> ErrorInfo(401, "AA"))
    }
}
