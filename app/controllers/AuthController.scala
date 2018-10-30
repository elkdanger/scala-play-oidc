package controllers

import java.net.URLEncoder

import auth.{MyAuthConfig, AuthService}
import javax.inject.{Inject, Singleton}
import play.api.libs.crypto.CookieSigner
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents, Cookie}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               ws: WSClient,
                               auth: AuthService,
                               cookieSigner: CookieSigner) extends AbstractController(cc) {

  def callback(code: String) = Action.async { implicit request =>
    auth.handleCodeCallback(code, auth.config) map { maybeUser =>
      maybeUser map { user =>
        // Now that we have an authenticated user, create a cookie with the username
        // and proceed to the admin page.
        Redirect(controllers.routes.AdminController.index())
          .withCookies(Cookie("auth-test", URLEncoder.encode(user.name)))

      } getOrElse Redirect(controllers.routes.HomeController.index())
    }
  }
}
