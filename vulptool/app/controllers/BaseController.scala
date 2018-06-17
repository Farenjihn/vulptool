package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsError, Reads}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class BaseController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )
}
