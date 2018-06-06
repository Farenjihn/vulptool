package controllers

import models.Event
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
      val events = route(app, request).get

      status(events) mustBe OK
      contentType(events) mustBe Some("application/json")
    }
  }

  "EventController POST" should {
    val event = Event(None, "added event", "", 1, 1, 1)

    "create a new event from the router" in {
      val request = FakeRequest(POST, "/event").withJsonBody(eventToJson.writes(event))
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
