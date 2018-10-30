package auth

import javax.inject.Inject
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class AuthService @Inject()(ws: WSClient, val config: MyAuthConfig) {

  def getAuthorizeRedirectUri(config: AuthConfig) = {

    // Build a URL to the /authorize endpoint on the auth server.
    // We will redirect the user to this so that they can sign in.

    UrlBuilder.buildUrl(config.authorizeUrl,
      "client_id" -> config.clientId,   // Auth server client id
      "redirect_uri" -> config.redirectUri,     // The URI that the auth server should send the code to
      "scope" -> "openid profile email",        // Scopes - must have 'openid' for OIDC
      "response_type" -> "code"                 // Use the 'authorization code' flow
    )
  }

  def handleCodeCallback(code: String, config: AuthConfig)(implicit ec: ExecutionContext): Future[Option[UserData]] = {

    // Take the code and swap it for an ID token, by calling the token endpoint on the auth server.
    // The auth server will send us back the access and id tokens

    val data = Json.toJson(TokenRequest(
      "authorization_code",                   // we're using the 'authorization code' flow
      "http://localhost:9000/auth/callback",  // The URI we specified in the redirect step
      code,   // The access code that the auth server sent us previously
      config  // The auth server config
    ))

    // Make the call. The response will be a JSON packet containing the tokens we need
    ws.url(config.tokenUrl).post(data) flatMap { r =>
      // This step is optional. If you don't want to know anything about the user,
      // you can skip this.
      // Sometimes the id token (a JWT) contains everything you need about the user,
      // but it depends on how you log in - Google and Facebook, for example, might not populate
      // this data reliably.
      getUserData(r.json, config)
    }
  }

  def getUserData(response: JsValue, config: AuthConfig)(implicit ec: ExecutionContext): Future[Option[UserData]] = {
    val tokens = getTokens(response) map {
      case (accessToken, _) =>

        // Use the access token to make a GET request to the auth server to get more information
        // about the user who was logged in.
        ws.url(config.userInfoUrl)
          .withQueryStringParameters("access_token" -> accessToken)
          .get map { userInfo =>

          // Parse the returned data into UserData
          val json = userInfo.json
          json.asOpt[UserData]
        }
    }

    tokens.getOrElse(Future.failed(new Exception("Unable to retrieve tokens")))
  }

  // Read the JSON response and extract the access and id tokens
  def getTokens(response: JsValue): Option[(String, String)] = for {
    accessToken <- (response \ "access_token").asOpt[String]
    idToken <- (response \ "id_token").asOpt[String]
  } yield (accessToken, idToken)
}
