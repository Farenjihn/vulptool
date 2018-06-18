package controllers

import dao.APITokenDAO
import javax.inject.{Inject, Singleton}
import models.APIToken
import play.api.libs.json.{JsError, Reads}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TokenAuthentication {
  self: AbstractController =>

  var tokenDAO: APITokenDAO = _

  def withAPIToken(f: => APIToken => Request[AnyContent] => Future[Result]) = Action.async { request =>
    request.headers.get("Authorization").flatMap { authHeader =>
      extractToken(authHeader).flatMap { bearer =>
        Await.result(tokenDAO.findByValue(bearer), Duration.Inf).map { token =>
          f(token)(request)
        }
      }
    }.getOrElse(Future {
      Unauthorized("Invalid token")
    })
  }


  def extractToken(authHeader: String) = {
    authHeader.split(" ") match {
      case Array(_, token) => Some(token)
      case _ => None
    }
  }
}

@Singleton
class BaseController @Inject()(cc: ControllerComponents, apiTokenDAO: APITokenDAO) extends AbstractController(cc) with TokenAuthentication {
  tokenDAO = apiTokenDAO

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )
}
