package controllers

import java.sql.Timestamp

import dao._
import javax.inject.{Inject, Singleton}
import models._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.ControllerComponents

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait EventSerialization extends MeetingSerialization with RaidSerialization with RosterSerialization {

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

  implicit val eventFullToJson: Writes[EventFull] = { event =>
    Json.obj(
      "id" -> event.id,
      "name" -> event.name,
      "description" -> event.description,
      "meeting" -> event.meeting,
      "raid" -> event.raid,
      "roster" -> event.roster
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

  implicit val jsonToEventFull: Reads[EventFull] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "description").read[String] and
      (JsPath \ "meeting").read[Meeting] and
      (JsPath \ "raid").read[Raid] and
      (JsPath \ "roster").read[Roster]
    ) (EventFull.apply _)
}

@Singleton
class EventController @Inject()(cc: ControllerComponents, eventDAO: EventDAO, meetingDAO: MeetingDAO, raidDAO: RaidDAO, rosterDAO: RosterDAO, apiTokenDAO: APITokenDAO)
  extends BaseController(cc, apiTokenDAO) with EventSerialization {

  //GET
  def getEvents = Action.async {
    val eventList = eventDAO.list()
    eventList.map(_.map(getFullEvent).sortBy(_.meeting.timeBegin.getTime()))
      .map(event => Ok(Json.toJson(event)))
  }

  //GET with id
  def getEvent(eventId: Int) = Action.async {
    val optionalEvent = eventDAO.findById(eventId)

    optionalEvent.map({
      case Some(event) =>
        Ok(Json.toJson(getFullEvent(event)))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the event does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Event #" + eventId + " not found.")
        ))
    })
  }

  def getFullEvent(event: Event): EventFull = {
    val meeting = Await.result(eventDAO.getMeetingOfEvent(event.id.get), Duration.Inf).get
    val raid = Await.result(eventDAO.getRaidOfEvent(event.id.get), Duration.Inf).get
    val roster = Await.result(eventDAO.getRosterOfEvent(event.id.get), Duration.Inf).get

    EventFull(event.id, event.name, event.description, meeting, raid, roster)
  }

  // GET with dates
  def getEventsByMeetingDate(begin: Long, end: Long) = Action.async {
    val optionalEvents = eventDAO.listFromDates(new Timestamp(begin * 1000L), new Timestamp(end * 1000L))

    optionalEvents.map(_.map(getFullEvent).sortBy(_.meeting.timeBegin.getTime()))
      .map(eventFull => Ok(Json.toJson(eventFull)))
  }

  //POST
  def postEvent = Action.async(validateJson[EventFull]) { request =>
    val eventFull = request.body

    val meeting = Await.result(meetingDAO.insert(eventFull.meeting), Duration.Inf)
    val raid = Await.result(raidDAO.insertIfNotExists(eventFull.raid), Duration.Inf)
    val roster = Await.result(rosterDAO.insert(eventFull.roster), Duration.Inf)
    val createdEvent = eventDAO.insert(
      Event(eventFull.id, eventFull.name, eventFull.description, meeting.id.get, raid.id.get, roster.id.get)
    )

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
  def updateEvent(eventId: Int) = Action.async(validateJson[EventFull]) { request =>
    val newEventFull = request.body
    eventDAO.update(eventId, EventFromFull(newEventFull)).map({
      case 1 =>
        meetingDAO.update(newEventFull.meeting.id.get, newEventFull.meeting)
        raidDAO.update(newEventFull.raid.id.get, newEventFull.raid)
        rosterDAO.update(newEventFull.roster.id.get, newEventFull.roster)

        Ok(
          Json.obj(
            "status" -> "OK",
            "message" -> ("Event '" + eventId + " " + newEventFull.name + "' updated.")
          )
        )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Event #" + eventId + " not found.")
      ))
    })
  }

  def EventFromFull(e: EventFull) =
    Event(e.id, e.name, e.description, e.meeting.id.get, e.raid.id.get, e.roster.id.get)

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
