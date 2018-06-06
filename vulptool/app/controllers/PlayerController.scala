package controllers

import javax.inject.{Inject, Singleton}
import models.Player
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import models.Player
import dao.PlayerDAO

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PlayerController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  implicit val playerToJson: Writes[Player] = { player =>
    Json.obj(
      "mainPseudo" -> player.mainPseudo,
      "token" -> player.token
    )
  }

  implicit val jsonToPlayer: Reads[Player] = (
    (JsPath \ "mainPseudo").read[String] and
      (JsPath \ "token").read[String]
    )(Player.apply _)

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getPlayers = Action.async {
    val jsonPlayerList = PlayerDAO.list()
    jsonPlayerList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def postPlayer = Action.async(validateJson[Player]) { request =>
    val player = request.body
    val createdPlayer = PlayerDAO.insert(player)

    postPlayer.map(s =>
      Ok(
        Json.obj(
          "status"  -> "OK",
          "id"      -> s.playerId,
          "message" -> ("Player '" + s.playerId + " " + s.mainPseudo + "' saved.")
        )
      )
    )
  }

  //GET with id
  def getPlayer(playerId: String) = Action.async {
    val optionalPlayer = PlayerDAO.findById(playerId)

    optionalPlayer.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the player does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Player #" + playerId + " not found.")
        ))
    }
  }

  //PUT
  def updatePlayer(playerId: String) = Action.async(validateJson[Player]) { request =>
    val newPlayer = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    PlayerDAO.update(playerId, newPlayer).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Player '" + newPlayer.playerId + " " + newPlayer.mainPseudo + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Player #" + playerId + " not found.")
      ))
    }
  }

  //DELETE
  def deletePlayer(playerId: String) = Action.async {
    PlayerDAO.delete(playerId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
          "message" -> ("Player #" + playerId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Player #" + playerId + " not found.")
      ))
    }
  }
}
