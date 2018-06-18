package controllers

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.github.t3hnar.bcrypt._
import dao.{APITokenDAO, PlayerDAO}
import javax.inject.{Inject, Singleton}
import models.{APIToken, Login, Player}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait PlayerSerialization {

  implicit val playerToJson: Writes[Player] = { player =>
    Json.obj(
      "id" -> player.id,
      "main_pseudo" -> player.mainPseudo
    )
  }

  implicit val playerInsertToJson: Writes[Login] = { playerInsert =>
    Json.obj(
      "main_pseudo" -> playerInsert.mainPseudo,
      "password" -> playerInsert.password
    )
  }

  implicit val jsonToPlayerInsert: Reads[Login] = (
    (JsPath \ "main_pseudo").read[String] and
      (JsPath \ "password").read[String]
    ) (Login.apply _)
}

@Singleton
class PlayerController @Inject()(cc: ControllerComponents, playerDAO: PlayerDAO, apiTokenDAO: APITokenDAO) extends BaseController(cc, apiTokenDAO) with PlayerSerialization {

  //GET
  def getPlayers = Action.async {
    val playerList = playerDAO.list()
    playerList.map(player => Ok(Json.toJson(player)))
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
  def postPlayer = Action.async(validateJson[Login]) { request =>
    val login = request.body
    val createdPlayer = playerDAO.insert(Player(None, login.mainPseudo, login.password.bcrypt, None, None))

    createdPlayer.map(player =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> player.id,
          "message" -> ("Player '" + player.id + " " + player.mainPseudo + "' saved.")
        )
      )
    )
  }

  //POST
  def postPlayerLogin = Action.async(validateJson[Login]) { request =>
    val login = request.body
    val players = Await.result(playerDAO.findByPseudo(login.mainPseudo), Duration.Inf)

    val playerId = players.map(player => (player.id, login.password.isBcrypted(player.hashedPassword)))
      .filter(tuple => tuple._2)
      .map(_._1).headOption

    playerId match {
      case Some(Some(id)) =>
        val apiToken = Await.result(apiTokenDAO.findByPlayerId(id).map {
          case Some(token) => token
          case None =>
            val uuid = UUID.randomUUID().toString
            Await.result(apiTokenDAO.insert(APIToken(None, id, uuid, Timestamp.from(Instant.now()))), Duration.Inf)
        }, Duration.Inf)

        Future {
          Ok(
            Json.obj(
              "token" -> apiToken.value
            )
          )
        }
      case None => Future {
        Unauthorized(
          Json.obj(
            "reason" -> "Username or password incorrect"
          )
        )
      }
    }
  }

  //DELETE
  def deletePlayer(playerId: Int) = withAPIToken { token => { request =>
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

}
