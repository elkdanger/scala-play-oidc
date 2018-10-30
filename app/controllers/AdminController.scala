package controllers

import java.net.URLDecoder

import auth.AuthAction
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}
import views.html.admin

@Singleton
class AdminController @Inject()(cc: ControllerComponents, authAction: AuthAction) extends AbstractController(cc) {

  def index() = authAction { implicit  request =>
    val name = URLDecoder.decode(request.cookies.get("auth-test").fold("") { c => c.value })
    Ok(admin(name))
  }

}
