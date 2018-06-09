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

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

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

  //POST
  def postFigure = Action.async(validateJson[Figure]) { request =>
    val figure = request.body
    val createdFigure = figureDAO.insert(figure)

    createdFigure.map(figure =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> figure.id,
          "message" -> ("Figure '" + figure.id + " " + figure.name + "' saved.")
        )
      ))
  }

  //PUT
  def updateFigure(figureId: Int) = Action.async(validateJson[Figure]) { request =>
    val newFigure = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    figureDAO.update(figureId, newFigure).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Figure '" + newFigure.id + " " + newFigure.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Figure #" + figureId + " not found.")
      ))
    })
  }

  //DELETE
  def deleteFigure(figureId: Int) = Action.async {
    figureDAO.delete(figureId).map({
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Figure #" + figureId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Figure #" + figureId + " not found.")
      ))
    })
  }

}
