package controllers

import dao.EventDAO
import javax.inject.{Inject, Singleton}
import models.Event
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

trait EventSerialization {

  implicit val eventToJson: Writes[Event] = { event =>
    Json.obj(
      "id" -> event.id,
      "name" -> event.name,
      "description" -> event.description,
      "meetingId" -> event.meetingId,
      "raidId" -> event.raidId,
      "rosterId" -> event.rosterId
    )
  }

  implicit val jsonToEvent: Reads[Event] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "description").read[String] and
      (JsPath \ "meetingId").read[Int] and
      (JsPath \ "raidId").read[Int] and
      (JsPath \ "rosterId").read[Int]
    ) (Event.apply _)
}

@Singleton
class EventController @Inject()(cc: ControllerComponents, eventDAO: EventDAO) extends AbstractController(cc) with EventSerialization {

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getEvents = Action.async {
    val jsonEventList = eventDAO.list()
    jsonEventList.map(event => Ok(Json.toJson(event)))
  }

  //GET with id
  def getEvent(eventId: Int) = Action.async {
    val optionalEvent = eventDAO.findById(eventId)

    optionalEvent.map({
      case Some(event) => Ok(Json.toJson(event))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the event does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Event #" + eventId + " not found.")
        ))
    })
  }

  //POST
  def postEvent = Action.async(validateJson[Event]) { request =>
    val event = request.body
    val createdEvent = eventDAO.insert(event)

    createdEvent.map(event =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> event.id,
          "message" -> ("Event '" + event.id + " " + event.name + "' saved.")
        )
      ))
  }

  //PUT
  def updateEvent(eventId: Int) = Action.async(validateJson[Event]) { request =>
    val newEvent = request.body

    eventDAO.update(eventId, newEvent).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Event '" + newEvent.id + " " + newEvent.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Event #" + eventId + " not found.")
      ))
    })
  }

  //DELETE
  def deleteEvent(eventId: Int) = Action.async {
    eventDAO.delete(eventId).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Event #" + eventId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Event #" + eventId + " not found.")
      ))
    })
  }
}
