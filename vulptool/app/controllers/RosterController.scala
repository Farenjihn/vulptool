package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class RosterController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val rosterToJson: Writes[Roster] = (
    (JsPath \ "rosterId").write[Int] and
      (JsPath \ "rosterType").write[String] and
      (JsPath \ "isDeleted").write[Boolean]
    )(unlift(Roster.unapply))

  implicit val jsonToRoster: Reads[Roster] = (
    (JsPath \ "rosterId").read[Int] and
      (JsPath \ "rosterType").read[String] (minLength[String](2)) and
      (JsPath \ "isDeleted").read[Boolean]
    )(Roster.apply _)

  def validateJson[A : Roster] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getRoster = Action.async {
    val jsonRosterList = RosterDAO.list()
    jsonRosterList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def createRoster = Action.async(validateJson[Roster]) { request =>
    val roster = request.body
    val createdRoster = RosterDAO.insert(roster)

    createdRoster.map(s =>
      Ok(
        Json.obj(
          "status"  -> "OK",
          "id"      -> s.rosterId,
          "message" -> ("Roster '" + s.rosterId + " " + s.rosterType + "' saved.")
        )
      )
    )
  }

  //GET with id
  def getRoster(rosterId: Long) = Action.async {
    val optionalRoster = RosterDAO.findById(rosterId)

    optionalRoster.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the roster does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Roster #" + rosterId + " not found.")
        ))
    }
  }

  //PUT
  def updateRoster(rosterId: Long) = Action.async(validateJson[Roster]) { request =>
    val newRoster = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    RosterDAO.update(rosterId, newRoster).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Roster '" + newRoster.rosterId + " " + newRoster.rosterType + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Roster #" + rosterId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteRoster(rosterId: Long) = Action.async {
    RosterDAO.delete(rosterId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
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
