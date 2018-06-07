package controllers

import dao.PlayerDAO
import javax.inject.{Inject, Singleton}
import models.Player
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

trait PlayerSerialization {

  implicit val playerToJson: Writes[Player] = { player =>
    Json.obj(
      "id" -> player.id,
      "main_pseudo" -> player.main_pseudo,
      "auth_code" -> player.auth_code,
      "access_code" -> player.access_code
    )
  }

  implicit val jsonToMeeting: Reads[Player] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "main_pseudo").read[String] and
      (JsPath \ "auth_code").read[String] and
      (JsPath \ "access_code").read[String]
  ) ((id, main_pseudo, auth_code, access_code) => Player(id, main_pseudo, auth_code, access_code))
}

@Singleton
class PlayerController @Inject()(cc: ControllerComponents, playerDAO: PlayerDAO) extends AbstractController(cc) with PlayerSerialization {

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getPlayers = Action.async {
    val jsonPlayerList = playerDAO.list()
    jsonPlayerList.map(player => Ok(Json.toJson(player)))
  }

  //GET with id
  def getPlayer(playerId: Int) = Action.async {
    val optionalPlayer = playerDAO.findById(playerId)

    optionalPlayer.map {
      case Some(player) => Ok(Json.toJson(player))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the player does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Player #" + playerId + " not found.")
        ))
    }
  }

  //POST
  def postPlayer = Action.async(validateJson[Player]) { request =>
    val player = request.body
    val createdPlayer = playerDAO.insert(player)

    createdPlayer.map(player =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> player.id,
          "message" -> ("Player '" + player.id + " " + player.main_pseudo + "' saved.")
        )
      )
    )
  }

  //PUT
  def updatePlayer(playerId: Int) = Action.async(validateJson[Player]) { request =>
    val newPlayer = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    playerDAO.update(playerId, newPlayer).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Player '" + newPlayer.id + " " + newPlayer.main_pseudo + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Player #" + playerId + " not found.")
      ))
    })
  }

  //DELETE
  def deletePlayer(playerId: Int) = Action.async {
    playerDAO.delete(playerId).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Player #" + playerId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Player #" + playerId + " not found.")
      ))
    })
  }
}
