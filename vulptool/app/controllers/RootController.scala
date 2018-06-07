package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

class RootController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  //GET
  def helloWorld = Action {
    Ok(views.html.index.render())
  }
}
