package controllers

import models.{Figure, WoWClass}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class FigureControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with FigureSerialization {
  var createdId = 0

  "FigureController GET" should {
    "return the list of figures from the router" in {
      val request = FakeRequest(GET, "/figure")
      val figures = route(app, request).get

      status(figures) mustBe OK
      contentType(figures) mustBe Some("application/json")
    }

    "return a specific figure from the router" in {
      val request = FakeRequest(GET, "/figure/1")
      val figure = route(app, request).get

      status(figure) mustBe OK
      contentType(figure) mustBe Some("application/json")
    }
  }

  "FigureController POST" should {
    val figure = Figure(None, "added figure", WoWClass.DeathKnight, 25, 130, 1)

    "create a new figure from the router" in {
      val request = FakeRequest(POST, "/figure").withJsonBody(figureToJson.writes(figure))
      val ret = route(app, request).get

      status(ret) mustBe OK
      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
    }
  }

  "FigureController DELETE" should {
    "delete the given figure from the router" in {
      val request = FakeRequest(DELETE, "/figure/" + createdId)
      val ret = route(app, request).get

      status(ret) mustBe OK
    }
  }
}

