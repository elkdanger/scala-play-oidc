package auth

import play.api.libs.json.Json

case class UserData(email: String, picture: String, sub: String, name: String)

object UserData {
  implicit val fmt = Json.format[UserData]
}
