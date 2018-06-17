package controllers

import models.Login
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class PlayerControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with PlayerSerialization {
  var createdId = 0

  "PlayerController GET" should {
    "return the list of players from the router" in {
      val request = FakeRequest(GET, "/player")
      val players = route(app, request).get

      status(players) mustBe OK
      contentType(players) mustBe Some("application/json")
    }

    "return a specific player from the router" in {
      val request = FakeRequest(GET, "/player/1")
      val events = route(app, request).get

      status(events) mustBe OK
      contentType(events) mustBe Some("application/json")
    }
  }

  "PlayerController POST" should {
    val player = Login("added player", "password")

    "create a new player from the router" in {
      val request = FakeRequest(POST, "/player").withJsonBody(playerInsertToJson.writes(player))
      val ret = route(app, request).get

      status(ret) mustBe OK
      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
    }
  }

  "PlayerController DELETE" should {
    "delete the given player from the router" in {
      val request = FakeRequest(DELETE, "/player/" + createdId)
      val ret = route(app, request).get

      status(ret) mustBe OK
    }
  }
}

