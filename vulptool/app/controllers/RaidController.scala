package controllers

import dao.{APITokenDAO, RaidDAO}
import javax.inject.{Inject, Singleton}
import models.{Raid, RaidDifficulty}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext.Implicits.global

trait RaidSerialization {

  implicit val raidToJson: Writes[Raid] = { raid =>
    Json.obj(
      "id" -> raid.id,
      "name" -> raid.name,
      "nb_boss" -> raid.nbBoss,
      "difficulty" -> raid.difficulty
    )
  }

  implicit val jsonToRaid: Reads[Raid] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "nb_boss").read[Int] and
      (JsPath \ "difficulty").read[String]
    ) ((id, name, nbBoss, difficulty) => Raid(id, name, nbBoss, RaidDifficulty.withName(difficulty)))
}

@Singleton
class RaidController @Inject()(cc: ControllerComponents, raidDAO: RaidDAO, apiTokenDAO: APITokenDAO) extends BaseController(cc, apiTokenDAO) with RaidSerialization {

  //GET
  def getRaids = Action.async {
    val raidList = raidDAO.list()
    raidList.map(raid => Ok(Json.toJson(raid)))
  }

  //GET with id
  def getRaid(raidId: Int) = Action.async {
    val optionalRaid = raidDAO.findById(raidId)

    optionalRaid.map {
      case Some(raid) => Ok(Json.toJson(raid))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the raid does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Raid #" + raidId + " not found.")
        ))
    }
  }

  //POST
  def postRaid = Action.async(validateJson[Raid]) { request =>
    val raid = request.body
    val createdRaid = raidDAO.insertIfNotExists(raid)

    createdRaid.map(raid =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> raid.id,
          "message" -> ("Raid '" + raid.id + " " + raid.name + "' saved.")
        )
      )
    )
  }

  //PUT
  def updateRaid(raidId: Int) = Action.async(validateJson[Raid]) { request =>
    val newRaid = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    raidDAO.update(raidId, newRaid).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Raid '" + newRaid.id + " " + newRaid.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Raid #" + raidId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteRaid(raidId: Int) = Action.async {
    raidDAO.delete(raidId).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Raid #" + raidId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Raid #" + raidId + " not found.")
      ))
    }
  }
}
