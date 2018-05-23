package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class PlayerController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val playerToJson: Writes[Player] = (
    (JsPath \ "playerId").write[Int] and
      (JsPath \ "mainPseudo").write[String] and
      (JsPath \ "token").write[String]
    )(unlift(Player.unapply))

  implicit val jsonToPlayer: Reads[Player] = (
    (JsPath \ "playerId").read[Int] and
      (JsPath \ "mainPseudo").read[String] (minLength[String](2)) and
      (JsPath \ "token").read[String] and
      (JsPath \ "isDeleted").read[Boolean]
    )(Player.apply _)

  def validateJson[A : Player] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getPlayer = Action.async {
    val jsonPlayerList = PlayerDAO.list()
    jsonPlayerList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def createPlayer = Action.async(validateJson[Player]) { request =>
    val player = request.body
    val createdPlayer = PlayerDAO.insert(player)

    createdPlayer.map(s =>
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
  def getPlayer(playerId: Long) = Action.async {
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
  def updatePlayer(playerId: Long) = Action.async(validateJson[Player]) { request =>
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
  def deletePlayer(playerId: Long) = Action.async {
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
