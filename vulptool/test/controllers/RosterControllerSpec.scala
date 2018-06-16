package controllers

import models.{Roster, RosterWithFigures}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class RosterControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with RosterSerialization {
  var createdId = 0

  "RosterController GET" should {
    "return the list of rosters from the router" in {
      val request = FakeRequest(GET, "/roster")
      val rosters = route(app, request).get

      status(rosters) mustBe OK
      contentType(rosters) mustBe Some("application/json")
    }

    "return a specific roster from the router" in {
      val request = FakeRequest(GET, "/roster/1")
      val events = route(app, request).get

      status(events) mustBe OK
      contentType(events) mustBe Some("application/json")
    }
  }

  "RosterController POST" should {
    val roster = RosterWithFigures(None, "added roster", List(1, 2))

    "create a new roster from the router" in {
      val request = FakeRequest(POST, "/roster").withJsonBody(rosterWithFiguresToJson.writes(roster))
      val ret = route(app, request).get

      status(ret) mustBe OK
      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
    }
  }

  "RosterController DELETE" should {
    "delete the given roster from the router" in {
      val request = FakeRequest(DELETE, "/roster/" + createdId)
      val ret = route(app, request).get

      status(ret) mustBe OK
    }
  }
}

