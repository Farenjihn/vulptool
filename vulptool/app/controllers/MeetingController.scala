package controllers

import dao.MeetingDAO
import javax.inject.{Inject, Singleton}
import models.Meeting
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MeetingController @Inject()(cc: ControllerComponents, meetingDAO: MeetingDAO) extends AbstractController(cc) {

  implicit val meetingToJson: Writes[Meeting] = { meeting =>
    Json.obj(
      "id" -> meeting.id,
      "time" -> meeting.time
    )
  }

  implicit val jsonToMeeting: Reads[Meeting] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "time").read[String](minLength[String](10) keepAnd maxLength[String](10)) //date format accepted: jj.mm.yyyy HH:MM:ss
    ) (Meeting.apply _)

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getMeetings = Action.async {
    val jsonMeetingList = meetingDAO.list()
    jsonMeetingList.map(meeting => Ok(Json.toJson(meeting)))
  }

  //GET with id
  def getMeeting(meetingId: Int) = Action.async {
    val optionalMeeting = meetingDAO.findById(meetingId)

    optionalMeeting.map {
      case Some(meeting) => Ok(Json.toJson(meeting))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the meeting does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Meeting #" + meetingId + " not found.")
        ))
    }
  }

  //POST
  def postMeeting = Action.async(validateJson[Meeting]) { request =>
    val meeting = request.body
    val createdMeeting = meetingDAO.insert(meeting)

    createdMeeting.map(meeting =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> meeting.id,
          "message" -> ("Meeting '" + meeting.id + " " + meeting.time + "' saved.")
        )
      )
    )
  }

  //PUT
  def updateMeeting(meetingId: Int) = Action.async(validateJson[Meeting]) { request =>
    val newMeeting = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    meetingDAO.update(meetingId, newMeeting).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Meeting '" + newMeeting.id + " " + newMeeting.time + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Meeting #" + meetingId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteMeeting(meetingId: Int) = Action.async {
    meetingDAO.delete(meetingId).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Meeting #" + meetingId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Meeting #" + meetingId + " not found.")
      ))
    }
  }
}
