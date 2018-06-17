package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
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
}

