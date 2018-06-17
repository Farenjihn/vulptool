package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TemplateController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getTemplates = play.mvc.Results.TODO

  def getTemplate(id: Int) = play.mvc.Results.TODO

  def postTemplate() = play.mvc.Results.TODO

  def deleteTemplate(id: Int) = play.mvc.Results.TODO

  def updateTemplate(id: Int) = play.mvc.Results.TODO
}

