package controllers

import java.sql.Timestamp

import models._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class EventControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with EventSerialization {
  var createdId = 0

  "EventController GET" should {
    "return the list of events from the router" in {
      val request = FakeRequest(GET, "/event")
      val events = route(app, request).get

      status(events) mustBe OK
      contentType(events) mustBe Some("application/json")
    }

    "return a specific event from the router" in {
      val request = FakeRequest(GET, "/event/1")
      val event = route(app, request).get

      status(event) mustBe OK
      contentType(event) mustBe Some("application/json")
    }
  }

  "EventController POST" should {
    val meeting = Meeting(None, Timestamp.valueOf("2018-06-07 08:33:26"), Timestamp.valueOf("2018-06-07 08:45:26"))
    val raid = Raid(None, "added raid", 99, RaidDifficulty.Mythic)
    val roster = Roster(None, "added roster")
    val eventFull = EventFull(None, "added eventFull", "", meeting, raid, roster)

    "create a new eventFull from the router" in {
      val request = FakeRequest(POST, "/event").withJsonBody(eventFullToJson.writes(eventFull))
      val ret = route(app, request).get

      status(ret) mustBe OK
      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
    }
  }

  "EventController DELETE" should {
    "delete the given event from the router" in {
      val request = FakeRequest(DELETE, "/event/" + createdId)
      val ret = route(app, request).get

      status(ret) mustBe OK
    }
  }
}
