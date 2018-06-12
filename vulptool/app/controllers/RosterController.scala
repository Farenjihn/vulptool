package controllers

import dao.RosterDAO
import javax.inject.{Inject, Singleton}
import models.{Figure, Roster, RosterFull}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

trait RosterSerialization extends FigureSerialization {

  implicit val rosterToJson: Writes[Roster] = { roster =>
    Json.obj(
      "id" -> roster.id,
      "name" -> roster.name
    )
  }

  implicit val rosterFullToJson: Writes[RosterFull] = { roster =>
    Json.obj(
      "id" -> roster.id,
      "name" -> roster.name,
      "figures" -> roster.figures.map(Json.toJson(_))
    )
  }

  implicit val jsonToRoster: Reads[Roster] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String]
    ) (Roster.apply _)

  implicit val jsonToRosterFull: Reads[RosterFull] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "figures").read[List[Figure]]
    ) (RosterFull.apply _)

}

@Singleton
class RosterController @Inject()(cc: ControllerComponents, rosterDAO: RosterDAO) extends AbstractController(cc) with RosterSerialization {

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getRosters = Action.async {
    val jsonRosterList = rosterDAO.list()
    jsonRosterList.map(roster => Ok(Json.toJson(roster)))
  }

  //GET with id and with figures
  def getRoster(rosterId: Int) = Action.async {
    val optionalRoster = rosterDAO.findById(rosterId)

    optionalRoster.map {
      case Some(roster) => {
        val figures = rosterDAO.getFiguresFromRoster(rosterId).map {
          figures => Some(figures)
        } recover {
          case timeout: java.util.concurrent.TimeoutException => None
        }
        val figuresActual = Await.result(figures, Duration.Inf)

        val rosterFull = RosterFull(roster.id, roster.name, figuresActual.getOrElse(Seq()).toList)
        Ok(Json.toJson(rosterFull))
      }
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the roster does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Roster #" + rosterId + " not found.")
        ))
    }
  }

  //POST
  def postRoster = Action.async(validateJson[Roster]) { request =>
    val roster = request.body
    val createdRoster = rosterDAO.insert(roster)

    createdRoster.map(roster =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> roster.id,
          "message" -> ("Roster '" + roster.id + " " + roster.name + "' saved.")
        )
      )
    )
  }

  //PUT
  def updateRoster(rosterId: Int) = Action.async(validateJson[Roster]) { request =>
    val newRoster = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    rosterDAO.update(rosterId, newRoster).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Roster '" + newRoster.id + " " + newRoster.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Roster #" + rosterId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteRoster(rosterId: Int) = Action.async {
    rosterDAO.delete(rosterId).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Roster #" + rosterId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Roster #" + rosterId + " not found.")
      ))
    }
  }
}
