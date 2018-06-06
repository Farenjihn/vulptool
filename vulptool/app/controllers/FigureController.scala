package controllers

import javax.inject.{Inject, Singleton}
import models.Figure
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import models.Figure
import models.Player
import dao.FigureDAO

@Singleton
class FigureController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val figureToJson: Writes[Figure] = { figure =>
    Json.obj(
      "id" -> figure.id,
      "name" -> figure.name,
      "fclasse" -> figure.fclasse,
      "lvl" -> figure.lvl,
      "ilvl" -> figure.ilvl,
      "playerId" -> figure.playerId
    )
  }

  implicit val jsonToFigure: Reads[Figure] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] (minLength[String](2)) and
      (JsPath \ "fclasse").read[String] (minLength[String](2)) and
      (JsPath \ "lvl").read[Int] and
      (JsPath \ "ilvl").read[Int] and
      (JsPath \ "playerId").read[Int]
    )(Figure.apply _)

  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  //GET
  def getFigures = Action.async {
    val jsonFigureList = FigureDAO.list()
    jsonFigureList map (s => Ok(Json.toJson(s)))
  }

  //POST
  def postFigure = Action.async(validateJson[Figure]) { request =>
    val figure = request.body
    val createdFigure = FigureDAO.insert(figure)

    postFigure.map(s =>
      Ok(
        Json.obj(
          "status"  -> "OK",
          "id"      -> s.figureId,
          "message" -> ("Figure '" + s.figureId + " " + s.name + "' saved.")
        )
      )
    )
  }

  //GET with id
  def getFigure(figureId: Long) = Action.async {
    val optionalFigure = FigureDAO.findById(figureId)

    optionalFigure.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the figure does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Figure #" + figureId + " not found.")
        ))
    }
  }

  //PUT
  def updateFigure(figureId: Long) = Action.async(validateJson[Figure]) { request =>
    val newFigure = request.body

    // Try to edit the student, then return a 200 OK HTTP status to the client if everything worked.
    FigureDAO.update(figureId, newFigure).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Figure '" + newFigure.figureId + " " + newFigure.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Figure #" + figureId + " not found.")
      ))
    }
  }

  //DELETE
  def deleteFigure(figureId: Long) = Action.async {
    FigureDAO.delete(figureId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
          "message" -> ("Figure #" + figureId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Figure #" + figureId + " not found.")
      ))
    }
  }

}
