package controllers

import javax.inject.{Inject, Singleton}
import models.Template
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TemplateController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getTemplate() = play.mvc.Results.TODO

  def getTemplatee(id: Int) = play.mvc.Results.TODO

  def postTemplate() = play.mvc.Results.TODO

  def deleteTemplate(id: Int) = play.mvc.Results.TODO

  def updateTemplate(id: Int) = play.mvc.Results.TODO
}

