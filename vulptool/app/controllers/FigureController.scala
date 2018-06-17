package controllers

import dao.FigureDAO
import javax.inject.{Inject, Singleton}
import models.{Figure, WoWClass}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

trait FigureSerialization {

  implicit val figureToJson: Writes[Figure] = { figure =>
    Json.obj(
      "id" -> figure.id,
      "name" -> figure.name,
      "fclass" -> figure.fclass,
      "lvl" -> figure.lvl,
      "ilvl" -> figure.ilvl,
      "player_id" -> figure.playerId
    )
  }

  implicit val jsonToFigure: Reads[Figure] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "fclass").read[String](minLength[String](2)) and
      (JsPath \ "lvl").read[Int] and
      (JsPath \ "ilvl").read[Double] and
      (JsPath \ "player_id").read[Int]
    ) ((id, name, fclass, lvl, ilvl, playerId) => Figure(id, name, WoWClass.withName(fclass), lvl, ilvl, playerId))
}

@Singleton
class FigureController @Inject()(cc: ControllerComponents, figureDAO: FigureDAO) extends AbstractController(cc) with FigureSerialization {

  //GET
  def getFigures = Action.async {
    val figureList = figureDAO.list()
    figureList.map(figure => Ok(Json.toJson(figure)))
  }

  //GET with id
  def getFigure(figureId: Int) = Action.async {
    val optionalFigure = figureDAO.findById(figureId)

    optionalFigure map {
      case Some(figure) => Ok(Json.toJson(figure))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the figure does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Figure #" + figureId + " not found.")
        ))
    }
  }
}
