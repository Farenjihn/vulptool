package controllers

import java.sql.Timestamp

import models.Meeting
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class MeetingControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MeetingSerialization {
  var createdId = 0

  "MeetingController GET" should {
    "return the list of meetings from the router" in {
      val request = FakeRequest(GET, "/meeting")
      val meetings = route(app, request).get

      status(meetings) mustBe OK
      contentType(meetings) mustBe Some("application/json")
    }

    "return a specific meeting from the router" in {
      val request = FakeRequest(GET, "/meeting/1")
      val meeting = route(app, request).get

      status(meeting) mustBe OK
      contentType(meeting) mustBe Some("application/json")
    }
  }

  "MeetingController POST" should {
    val meeting = Meeting(None, Timestamp.valueOf("2018-06-07 08:33:26"), Timestamp.valueOf("2018-06-07 08:45:26"))

    "create a new meeting from the router" in {
      val request = FakeRequest(POST, "/meeting").withJsonBody(meetingToJson.writes(meeting))
      val ret = route(app, request).get

      status(ret) mustBe OK
      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
    }
  }

  "MeetingController DELETE" should {
    "delete the given meeting from the router" in {
      val request = FakeRequest(DELETE, "/meeting/" + createdId)
      val ret = route(app, request).get

      status(ret) mustBe OK
    }
  }
}

