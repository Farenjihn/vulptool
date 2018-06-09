//package controllers
//
//import models.{Raid, RaidDifficulty}
//import org.scalatestplus.play._
//import org.scalatestplus.play.guice._
//import play.api.libs.json.Json
//import play.api.test.Helpers._
//import play.api.test._
//
//class RaidControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with RaidSerialization {
//  var createdId = 0
//
//  "RaidController GET" should {
//    "return the list of raids from the router" in {
//      val request = FakeRequest(GET, "/raid")
//      val raids = route(app, request).get
//
//      status(raids) mustBe OK
//      contentType(raids) mustBe Some("application/json")
//    }
//
//    "return a specific raid from the router" in {
//      val request = FakeRequest(GET, "/raid/1")
//      val events = route(app, request).get
//
//      status(events) mustBe OK
//      contentType(events) mustBe Some("application/json")
//    }
//  }
//
//  "RaidController POST" should {
//    val raid = Raid(None, "added raid", 99, RaidDifficulty.Mythic)
//
//    "create a new raid from the router" in {
//      val request = FakeRequest(POST, "/raid").withJsonBody(raidToJson.writes(raid))
//      val ret = route(app, request).get
//
//      status(ret) mustBe OK
//      createdId = (Json.parse(contentAsString(ret)) \ "id").get.as[Int]
//    }
//  }
//
//  "RaidController DELETE" should {
//    "delete the given raid from the router" in {
//      val request = FakeRequest(DELETE, "/raid/" + createdId)
//      val ret = route(app, request).get
//
//      status(ret) mustBe OK
//    }
//  }
//}
