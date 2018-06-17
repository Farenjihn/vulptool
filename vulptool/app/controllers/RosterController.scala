package controllers

import dao.{APITokenDAO, FigureRosterDAO, RosterDAO}
import javax.inject.{Inject, Singleton}
import models._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.ControllerComponents

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

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

  implicit val rosterWithFiguresToJson: Writes[RosterWithFigures] = { roster =>
    Json.obj(
      "id" -> roster.id,
      "name" -> roster.name,
      "figures" -> roster.figures
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

  implicit val jsonToRosterWithFigures: Reads[RosterWithFigures] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "figures").read[List[Int]]
    ) (RosterWithFigures.apply _)
}

@Singleton
class RosterController @Inject()(cc: ControllerComponents, rosterDAO: RosterDAO, figureRosterDAO: FigureRosterDAO, apiTokenDAO: APITokenDAO) extends BaseController(cc, apiTokenDAO) with RosterSerialization {

  //GET
  def getRosters = Action.async {
    val jsonRosterList = rosterDAO.list()
    jsonRosterList.map(_.map(getRosterWithFigures)).map(roster => Ok(Json.toJson(roster)))
  }

  //GET with id and with figures
  def getRoster(rosterId: Int) = Action.async {
    val optionalRoster = rosterDAO.findById(rosterId)

    optionalRoster.map {
      case Some(roster) =>
        Ok(Json.toJson(getRosterWithFigures(roster)))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the roster does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Roster #" + rosterId + " not found.")
        ))
    }
  }

  def getRosterWithFigures(roster: Roster): RosterFull = {
    val figuresActual = Await.result(rosterDAO.getFiguresFromRoster(roster.id.get), Duration.Inf)
    RosterFull(roster.id, roster.name, figuresActual.toList)
  }

  //POST
  def postRoster = Action.async(validateJson[RosterWithFigures]) { request =>
    val rosterWithFigures = request.body
    val createdRoster = Await.result(rosterDAO.insert(Roster(None, rosterWithFigures.name)), Duration.Inf)
    val result = figureRosterDAO.insertFiguresWithRoster(rosterWithFigures.figures.map(f => FigureRoster(f, createdRoster.id.get)))

    result.map(_ =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> createdRoster.id,
          "message" -> ("Roster '" + createdRoster.id + " " + createdRoster.name + "'.")
        )
      )
    )
  }

  //PUT
  def updateRoster(rosterId: Int) = Action.async(validateJson[RosterWithFigures]) { request =>
    val newRoster = request.body

    rosterDAO.update(rosterId, Roster(newRoster.id, newRoster.name)).map {
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
