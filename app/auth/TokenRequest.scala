package auth

import play.api.libs.json.Json

case class TokenRequest(grant_type: String,
                        redirect_uri: String,
                        code: String,
                        client_id: String,
                        client_secret: String
                       )

object TokenRequest {
  implicit val format = Json.format[TokenRequest]

  def apply(grantType: String, redirectUri: String, code: String, config: AuthConfig): TokenRequest =
    TokenRequest(grantType, redirectUri, code, config.clientId, config.clientSecret)
}
