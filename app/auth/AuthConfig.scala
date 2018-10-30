package auth

import javax.inject.Inject
import play.api.Configuration

trait AuthConfig {
  def clientId: String
  def clientSecret: String
  def domain: String
  def redirectUri: String
  def authorizeUrl: String
  def tokenUrl: String
  def userInfoUrl: String
}

class MyAuthConfig @Inject()(config: Configuration) extends AuthConfig {
  override def clientId: String = config.get[String]("auth.clientId")
  override def clientSecret: String = config.get[String]("auth.secretKey")
  override def domain: String = config.get[String]("auth.domain")
  override val redirectUri: String = config.get[String]("auth.redirectUri")
  override def tokenUrl = s"https://$domain/oauth/token"
  override def authorizeUrl = s"https://$domain/authorize"
  override def userInfoUrl = s"https://$domain/userinfo"
}
