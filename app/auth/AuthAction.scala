package auth

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Use this action type to secure an endpoint, making sure that the user has to be logged in
  * before they can access it.
  */
class AuthAction @Inject()(parser: BodyParsers.Default, auth: AuthService)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]) = {
    if (request.cookies.get("auth-test").isEmpty) {
      Future.successful(Results.Redirect(auth.getAuthorizeRedirectUri(auth.config)))
    } else {
      block(request)
    }
  }
}
