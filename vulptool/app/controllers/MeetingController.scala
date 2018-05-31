package controllers

import javax.inject.{Inject, Singleton}
import models.Meeting
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import models.Meeting

@Singleton
class MeetingController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val meetingToJson: Writes[Meeting] = (
    (JsPath \ "meetingId").write[Int] and
      (JsPath \ "date").write[String] and
      (JsPath \ "time").write[String]
    )(unlift(Meeting.unapply))

  implicit val jsonToMeeting: Reads[Meeting] = (
    (JsPath \ "meetingId").read[Int] and
      (JsPath \ "date").read[String] (minLength[String](10) keepAnd maxLength[String](10)) and //date format accepted: jj.mm.yyyy
      (JsPath \ "time").read[String] (minLength[String](5) keepAnd maxLength[String](5)) //time format accepted: hh:mm
    )(Meeting.apply _)

  def validateJson[A : Meeting] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getMeeting = Action.async {
    val jsonMeetingList = MeetingDAO.list()
    jsonMeetingList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def postMeeting = Action.async(validateJson[Meeting]) { request =>
    val meeting = request.body
    val createdMeeting = MeetingDAO.insert(meeting)

    postMeeting.map(s =>
      Ok(
        Json.obj(
          "status"  -> "OK",
          "id"      -> s.meetingId,
          "message" -> ("Meeting '" + s.meetingId + " " + s.date + " " + s.time + "' saved.")
        )
      )
    )
  }

  //GET with id
  def getMeeting(meetingId: Long) = Action.async {
    val optionalMeeting = MeetingDAO.findById(meetingId)

    optionalMeeting.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the meeting does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Meeting #" + meetingId + " not found.")
        ))
    }
  }

  //PUT
  def updateMeeting(meetingId: Long) = Action.async(validateJson[Meeting]) { request =>
    val newMeeting = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    MeetingDAO.update(meetingId, newMeeting).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Meeting '" + newMeeting.meetingId + " " + s.date + " " + s.time + + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Meeting #" + meetingId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteMeeting(meetingId: Long) = Action.async {
    MeetingDAO.delete(meetingId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
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
