package controllers

import javax.inject.{Inject, Singleton}
import models.Event
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import models.Event
import models.Player

@Singleton
class EventController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val eventToJson: Writes[Event] = { event =>
    Json.obj(
      "id" -> event.id
      "name" -> event.name,
      "etype" -> event.etype,
      "meeting" -> event.meeting,
      "raid" -> event.raid,
      "rosters" -> event.rosters
    )
  }

  implicit val jsonToEvent: Reads[Event] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] (minLength[String](2)) and
      (JsPath \ "etype").read[String] and
      (JsPath \ "meeting").read[Int]  and
      (JsPath \ "raid").read[Int] and
      (JsPath \ "rosters").read[Player]
    )(Event.apply _)

  def validateJson[A : Event] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getEvent = Action.async {
    val jsonEventList = EventDAO.list()
    jsonEventList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def postEvent = Action.async(validateJson[Event]) { request =>
    val event = request.body
    val createdEvent = EventDAO.insert(event)

    postEvent.map(s =>
      Ok(
        Json.obj(
          "status"  -> "OK",
          "id"      -> s.eventId,
          "message" -> ("Event '" + s.eventId + " " + s.name + "' saved.")
        )
      )
    )
  }

  //GET with id
  def getEvent(eventId: Long) = Action.async {
    val optionalEvent = EventDAO.findById(eventId)

    optionalEvent.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the event does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Event #" + eventId + " not found.")
        ))
    }
  }

  //PUT
  def updateEvent(eventId: Long) = Action.async(validateJson[Event]) { request =>
    val newEvent = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    EventDAO.update(eventId, newEvent).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Event '" + newEvent.eventId + " " + newEvent.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Event #" + eventId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteEvent(eventId: Long) = Action.async {
    EventDAO.delete(eventId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
          "message" -> ("Event #" + eventId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Event #" + eventId + " not found.")
      ))
    }
  }
}
